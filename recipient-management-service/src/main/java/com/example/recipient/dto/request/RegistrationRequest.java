package com.example.recipient.dto.request;

public record RegistrationRequest(
        String email,
        String password
) {
}
