package com.example.security.controller;

import com.example.security.dto.request.AuthenticationRequest;
import com.example.security.dto.response.AuthenticationResponse;
import com.example.security.entity.Client;
import com.example.security.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final ClientService clientService;

    @PostMapping("/register")
    @Operation(summary = "register new Client with given credentials")
    public ResponseEntity<Boolean> register(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(clientService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "authenticate Client with existing credentials")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(clientService.authenticate(request));
    }

    @GetMapping("/validate")
    @Operation(summary = "validate given JWT and return Client ID")
    public ResponseEntity<Long> isTokenValid(
            @AuthenticationPrincipal Client client
    ) {
        return ResponseEntity.ok(client.getId());
    }
}
