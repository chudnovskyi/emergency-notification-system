package com.example.template.controller;

import com.example.template.dto.request.RecipientListRequest;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.service.TemplateRecipientsService;
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
public class TemplateRecipientsController {

    private final TemplateRecipientsService templateRecipientsService;

    @PostMapping("/{id}/recipients")
    @Operation(summary = "add Recipients to a Template")
    public ResponseEntity<TemplateResponse> addRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateRecipientsService.addRecipients(clientId, templateId, request));
    }

    @DeleteMapping("/{id}/recipients")
    @Operation(summary = "remove Recipients from a Template")
    public ResponseEntity<TemplateResponse> removeRecipients(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(OK).body(templateRecipientsService.removeRecipients(clientId, templateId, request));
    }
}
