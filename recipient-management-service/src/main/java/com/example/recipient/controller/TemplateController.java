package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("/")
    @Operation(summary = "create a Template")
    public ResponseEntity<TemplateResponse> create(
            @RequestHeader Long clientId,
            @RequestBody @Valid TemplateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.create(clientId, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a Template by ID")
    public ResponseEntity<TemplateResponse> get(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateService.get(clientId, templateId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a Template by ID")
    public ResponseEntity<Boolean> delete(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateService.delete(clientId, templateId));
    }

    @PostMapping("/{id}/recipients")
    @Operation(summary = "add Recipients to a Template")
    public ResponseEntity<TemplateResponse> addRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.addRecipients(clientId, templateId, request));
    }

    @DeleteMapping("/{id}/recipients")
    @Operation(summary = "remove Recipients from a Template")
    public ResponseEntity<TemplateResponse> removeRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.removeRecipients(clientId, templateId, request));
    }
}
