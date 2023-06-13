package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipients")
public class RecipientController {

    private final RecipientService recipientService;

    @PostMapping("/")
    public ResponseEntity<Long> register(@RequestBody RecipientRequest request) {
        return ResponseEntity.status(CREATED).body(recipientService.register(request));
    }
}
