package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.service.RecipientService;
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
    public ResponseEntity<Long> register(@RequestBody RecipientRequest request) {
        return ResponseEntity.status(CREATED).body(recipientService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipientResponse> receive(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(recipientService.receive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(recipientService.delete(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipientResponse> update(@PathVariable Long id, @RequestBody RecipientRequest request) {
        return ResponseEntity.status(OK).body(recipientService.update(id, request));
    }
}
