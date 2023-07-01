package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.service.RecipientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            @RequestHeader Long clientId,
            @RequestBody @Valid RecipientRequest request
    ) {
        return ResponseEntity.status(CREATED).body(recipientService.register(clientId, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "receive Recipient information by ID")
    public ResponseEntity<RecipientResponse> receive(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    ) {
        return ResponseEntity.status(OK).body(recipientService.receive(clientId, recipientId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a Recipient by ID")
    public ResponseEntity<Boolean> delete(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    ) {
        return ResponseEntity.status(OK).body(recipientService.delete(clientId, recipientId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "update a Recipient by ID")
    public ResponseEntity<RecipientResponse> update(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId,
            @RequestBody @Valid RecipientRequest request
    ) {
        return ResponseEntity.status(OK).body(recipientService.update(clientId, recipientId, request));
    }
}
