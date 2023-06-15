package com.example.recipient.dto.request;

public record AuthenticationRequest(
        String email,
        String password
) {
}
