package com.example.template.controller;

import com.example.template.dto.request.NotificationOptionsRequest;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.service.TemplateResponsesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateResponsesController {

    private final TemplateResponsesService templateResponsesService;

    @DeleteMapping("/{id}/options")
    @Operation(summary = "add Response options for a Template")
    public ResponseEntity<TemplateResponse> addResponseOptions(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid NotificationOptionsRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateResponsesService.addResponseOptions(clientId, templateId, request));
    }
}
