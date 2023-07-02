package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateResponse;
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

    public TemplateResponse create(Long clientId, TemplateRequest request) {
        if (templateRepository.existsTemplateByClientIdAndTitle(clientId, request.title())) {
            throw new TemplateTitleAlreadyExistsException(
                    message.getProperty("template.title_already_exists", request.title(), clientId)
            );
        }

        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(templateRepository::save)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateCreationException(
                        message.getProperty("template.creation", clientId)
                ));
    }

    public TemplateResponse get(Long clientId, Long templateId) {
        return templateRepository.findByIdAndClientId(templateId, clientId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));
    }

    public Boolean delete(Long clientId, Long templateId) {
        return templateRepository.findByIdAndClientId(templateId, clientId)
                .map(template -> {
                    templateRepository.delete(template);
                    return template;
                })
                .isPresent();
    }

    public TemplateResponse addRecipients(Long clientId, Long templateId, RecipientListRequest request) {
        Template template = templateRepository.findByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));

        for (Long recipientId : request.recipientIds()) {
            if (templateRepository.existsByIdAndRecipientsId(templateId, recipientId)) {
                log.warn("Recipient {} has already been registered for Template {}", recipientId, templateId);
                continue;
            }

            recipientRepository.findByIdAndClientId(recipientId, clientId)
                    .ifPresentOrElse(
                            template::addRecipient,
                            () -> log.warn("Recipient {} not found for Client {}", recipientId, clientId)
                    );
            templateRepository.save(template);
        }

        return mapper.mapToResponse(template);
    }

    public TemplateResponse removeRecipients(Long clientId, Long templateId, RecipientListRequest request) {
        Template template = templateRepository.findByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));

        for (Long recipientId : request.recipientIds()) {
            if (templateRepository.existsByIdAndRecipientsId(templateId, recipientId)) {
                recipientRepository.findByIdAndClientId(recipientId, clientId)
                        .ifPresent(template::removeRecipient);
            } else {
                log.warn("Recipient {} hasn't been registered for Template {}", recipientId, templateId);
            }
        }

        templateRepository.save(template);
        return mapper.mapToResponse(template);
    }
}
