package com.example.template.service;

import com.example.template.client.RecipientClient;
import com.example.template.client.ShortenerClient;
import com.example.template.dto.request.NotificationOptionsRequest;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.exception.template.TemplateNotFoundException;
import com.example.template.mapper.TemplateMapper;
import com.example.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateResponsesService {

    private final TemplateRepository templateRepository;
    private final RecipientClient recipientClient;
    private final ShortenerClient shortenerClient;
    private final MessageSourceService message;
    private final TemplateMapper mapper;

    public TemplateResponse addResponseOptions(Long clientId, Long templateId, NotificationOptionsRequest request) {
        return templateRepository.findByIdAndClientId(templateId, clientId)
                .map(template -> template.addResponse(shortenerClient.create(request).getBody()))
                .map(templateRepository::saveAndFlush)
                .map(template -> mapper.mapToResponse(template, recipientClient))
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));
    }
}
