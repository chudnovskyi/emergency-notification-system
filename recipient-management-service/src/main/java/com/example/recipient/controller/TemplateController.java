package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal Client client,
            @RequestBody @Valid TemplateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.create(client, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a Template by ID")
    public ResponseEntity<TemplateResponse> get(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(templateService.get(client.getId(), id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a Template by ID")
    public ResponseEntity<Boolean> delete(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(templateService.delete(client, id));
    }

    @PostMapping("/{id}/recipients")
    @Operation(summary = "add Recipients to a Template")
    public ResponseEntity<TemplateResponse> addRecipients(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.addRecipients(client.getId(), id, request));
    }

    @DeleteMapping("/{id}/recipients")
    @Operation(summary = "remove Recipients from a Template")
    public ResponseEntity<TemplateResponse> removeRecipients(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.removeRecipients(client.getId(), id, request));
    }
}
