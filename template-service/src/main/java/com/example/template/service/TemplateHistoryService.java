package com.example.template.service;

import com.example.template.dto.response.TemplateHistoryResponse;
import com.example.template.entity.Template;
import com.example.template.entity.TemplateHistory;
import com.example.template.exception.history.HistoryNotFoundException;
import com.example.template.exception.template.TemplateNotFoundException;
import com.example.template.mapper.TemplateMapper;
import com.example.template.repository.TemplateHistoryRepository;
import com.example.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateHistoryService {

    private final TemplateHistoryRepository templateHistoryRepository;
    private final TemplateRepository templateRepository;
    private final MessageSourceService message;
    private final TemplateMapper mapper;

    public TemplateHistoryResponse create(Long clientId, Long templateId) {
        Template template = templateRepository.findByIdAndClientId(templateId, clientId)
                .orElseThrow(() -> new TemplateNotFoundException(
                        message.getProperty("template.not_found", templateId, clientId)
                ));

        Optional<TemplateHistory> optTemplateHistory = templateHistoryRepository.findByClientIdAndResponseIdAndTitleAndContent(
                clientId,
                template.getResponseId(),
                template.getTitle(),
                template.getContent()
        );
        if (optTemplateHistory.isPresent()) {
            return mapper.mapToTemplateHistoryResponse(optTemplateHistory.get());
        }

        return Optional.of(template)
                .map(mapper::mapToTemplateHistory)
                .map(templateHistory -> templateHistory.addClient(clientId))
                .map(templateHistoryRepository::saveAndFlush)
                .map(mapper::mapToTemplateHistoryResponse)
                .orElseThrow(() -> new HistoryNotFoundException(
                        message.getProperty("history.creation", templateId)
                ));
    }

    public TemplateHistoryResponse get(Long clientId, Long historyId) {
        return templateHistoryRepository.findByIdAndClientId(historyId, clientId)
                .map(mapper::mapToTemplateHistoryResponse)
                .orElseThrow(() -> new HistoryNotFoundException(
                        message.getProperty("history.not_found", historyId, clientId)
                ));
    }
}
