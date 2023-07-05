package com.example.template.controller;

import com.example.template.dto.response.TemplateHistoryResponse;
import com.example.template.service.TemplateHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates/history")
public class TemplateHistoryController {

    private final TemplateHistoryService templateHistoryService;

    @PostMapping("/{id}")
    public ResponseEntity<TemplateHistoryResponse> create(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateHistoryService.create(clientId, templateId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateHistoryResponse> get(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(templateHistoryService.get(clientId, templateId));
    }
}
