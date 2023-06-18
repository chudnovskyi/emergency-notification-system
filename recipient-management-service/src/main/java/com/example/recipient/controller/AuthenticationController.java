package com.example.recipient.controller;

import com.example.recipient.dto.request.AuthenticationRequest;
import com.example.recipient.dto.request.RegistrationRequest;
import com.example.recipient.dto.response.AuthenticationResponse;
import com.example.recipient.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final ClientService clientService;

    @PostMapping("/register")
    @Operation(summary = "register new Client with given credentials")
    public ResponseEntity<Boolean> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(clientService.register(request));
    }

    @PostMapping("/authenticate")
    @Operation(summary = "authenticate Client with existing credentials")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(clientService.authenticate(request));
    }
}
