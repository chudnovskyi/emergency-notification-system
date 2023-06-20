package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.service.RecipientService;
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
@RequestMapping("/api/v1/recipients")
public class RecipientController {

    private final RecipientService recipientService;

    @PostMapping("/")
    @Operation(summary = "register a Recipient")
    public ResponseEntity<RecipientResponse> register(
            @AuthenticationPrincipal Client client,
            @RequestBody @Valid RecipientRequest request
    ) {
        return ResponseEntity.status(CREATED).body(recipientService.register(client, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "receive Recipient information by ID")
    public ResponseEntity<RecipientResponse> receive(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(recipientService.receive(client, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a Recipient by ID")
    public ResponseEntity<Boolean> delete(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(recipientService.delete(client, id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "update a Recipient by ID")
    public ResponseEntity<RecipientResponse> update(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id,
            @RequestBody @Valid RecipientRequest request
    ) {
        return ResponseEntity.status(OK).body(recipientService.update(client, id, request));
    }
}
