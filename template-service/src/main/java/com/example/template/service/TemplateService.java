package com.example.template.service;

import com.example.template.client.RecipientClient;
import com.example.template.dto.request.RecipientListRequest;
import com.example.template.dto.request.TemplateRequest;
import com.example.template.dto.response.RecipientResponse;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.entity.Template;
import com.example.template.exception.template.TemplateCreationException;
import com.example.template.exception.template.TemplateNotFoundException;
import com.example.template.exception.template.TemplateTitleAlreadyExistsException;
import com.example.template.mapper.TemplateMapper;
import com.example.template.repository.RecipientIdRepository;
import com.example.template.repository.TemplateRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final RecipientIdRepository recipientIdRepository;
    private final RecipientClient recipientClient;
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
                .map(template -> template.addClient(clientId))
                .map(templateRepository::save)
                .map(template -> mapper.mapToResponse(template, recipientClient))
                .orElseThrow(() -> new TemplateCreationException(
                        message.getProperty("template.creation", clientId)
                ));
    }

    public TemplateResponse get(Long clientId, Long templateId) {
        return templateRepository.findByIdAndClientId(templateId, clientId)
                .map(template -> mapper.mapToResponse(template, recipientClient))
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
            if (recipientIdRepository.existsByTemplateIdAndRecipientId(templateId, recipientId)) {
                log.warn("Recipient {} has already been registered for Template {}", recipientId, templateId);
                continue;
            }

            try {
                Optional.of(recipientClient.receiveRecipientById(clientId, recipientId))
                        .map(ResponseEntity::getBody)
                        .ifPresent(recipientResponse -> template.addRecipient(recipientResponse.id()));
                templateRepository.save(template);
            } catch (FeignException.NotFound e) {
                log.warn("Recipient {} not found for Client {}", recipientId, clientId);
            }
        }

        return mapper.mapToResponse(template, recipientClient);
    }

    public TemplateResponse removeRecipients(Long clientId, Long templateId, RecipientListRequest request) {
        Template template = templateRepository.findByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));

        for (Long recipientId : request.recipientIds()) {
            if (templateRepository.existsByIdAndRecipientIds_recipientId(templateId, recipientId)) {
                Optional.of(recipientClient.receiveRecipientById(clientId, recipientId))
                        .map(ResponseEntity::getBody)
                        .map(RecipientResponse::id)
                        .ifPresent(template::removeRecipient);
            } else {
                log.warn("Recipient {} hasn't been registered for Template {}", recipientId, templateId);
            }
        }

        templateRepository.save(template);
        return mapper.mapToResponse(template, recipientClient);
    }
}
