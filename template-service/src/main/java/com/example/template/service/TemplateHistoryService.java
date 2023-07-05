package com.example.template.service;

import com.example.template.dto.response.TemplateHistoryResponse;
import com.example.template.mapper.TemplateMapper;
import com.example.template.repository.TemplateHistoryRepository;
import com.example.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateHistoryService {

    private final TemplateHistoryRepository templateHistoryRepository;
    private final TemplateRepository templateRepository;
    private final TemplateMapper mapper;

    public TemplateHistoryResponse create(Long clientId, Long templateId) {
        return templateRepository.findByIdAndClientId(templateId, clientId) // TODO: retrieve existing if the fields repeats
                .map(mapper::mapToTemplateHistory)
                .map(templateHistory -> templateHistory.addClient(clientId))
                .map(templateHistoryRepository::saveAndFlush)
                .map(mapper::mapToTemplateHistoryResponse)
                .orElseThrow(); // TODO
    }

    public TemplateHistoryResponse get(Long clientId, Long templateId) {
        return templateHistoryRepository.findByIdAndClientId(templateId, clientId)
                .map(mapper::mapToTemplateHistoryResponse)
                .orElseThrow(); // TODO
    }
}
