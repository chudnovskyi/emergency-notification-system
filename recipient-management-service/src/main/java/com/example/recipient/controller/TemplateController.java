package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.service.TemplateService;
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
    public ResponseEntity<TemplateResponse> create(
            @AuthenticationPrincipal Client client,
            @RequestBody @Valid TemplateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.create(client, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> get(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(templateService.get(client.getId(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(templateService.delete(client, id));
    }

    @PostMapping("/add-recipients")
    public ResponseEntity<TemplateResponse> addRecipients(
            @AuthenticationPrincipal Client client,
            @RequestBody @Valid RecipientListRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.addRecipients(client.getId(), request));
    }
}
