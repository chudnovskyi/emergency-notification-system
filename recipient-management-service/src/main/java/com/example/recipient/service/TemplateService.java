package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.entity.Recipient;
import com.example.recipient.entity.Template;
import com.example.recipient.exception.template.TemplateCreationException;
import com.example.recipient.exception.template.TemplateNotFoundException;
import com.example.recipient.exception.template.TemplateTitleAlreadyExistsException;
import com.example.recipient.mapper.TemplateMapper;
import com.example.recipient.repository.RecipientRepository;
import com.example.recipient.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final RecipientRepository recipientRepository;
    private final MessageSourceService message;
    private final TemplateMapper mapper;

    public TemplateResponse create(Client client, TemplateRequest request) {
        if (templateRepository.existsTemplateByClient_IdAndTitle(client.getId(), request.title())) {
            throw new TemplateTitleAlreadyExistsException(
                    message.getProperty("template.title_already_exists", request.title(), client.getId())
            );
        }

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(client::addTemplate)
                .map(templateRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateCreationException(
                        message.getProperty("template.creation", client.getId())
                ));
    }

    public TemplateResponse get(Long clientId, Long templateId) {
        return templateRepository.findByIdAndClient_Id(templateId, clientId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));
    }

    public Boolean delete(Client client, Long templateId) {
        return templateRepository.findByIdAndClient_Id(templateId, client.getId())
                .map(template -> {
                    templateRepository.delete(template);
                    return template;
                })
                .isPresent();
    }

    public TemplateResponse addRecipients(Long clientId, RecipientListRequest request) {
        Long templateId = request.templateId();
        Template template = templateRepository.findByIdAndClient_Id(templateId, clientId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));

        for (Long recipientId : request.recipientIds()) {
            if (templateRepository.existsByIdAndRecipientsId(templateId, recipientId)) {
                log.warn("Recipient {} already registered for Template {}", recipientId, templateId);
                continue;
            }

            Optional<Recipient> maybeRecipient = recipientRepository.findByIdAndClient_Id(recipientId, clientId);
            if (maybeRecipient.isPresent()) {
                Recipient recipient = maybeRecipient.get();
                template.addRecipient(recipient);
            } else {
                log.warn("Recipient {} not found for Client {}", recipientId, clientId);
            }
        }

        templateRepository.save(template);
        return mapper.mapToResponse(template);
    }
}
