package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.service.RecipientService;
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
    public ResponseEntity<RecipientResponse> register(
            @AuthenticationPrincipal Client client,
            @RequestBody RecipientRequest request
    ) {
        return ResponseEntity.status(CREATED).body(recipientService.register(client, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipientResponse> receive(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(recipientService.receive(client, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(recipientService.delete(client, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipientResponse> update(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id,
            @RequestBody RecipientRequest request
    ) {
        return ResponseEntity.status(OK).body(recipientService.update(client, id, request));
    }
}
