package com.example.recipient.dto.response;

public record RecipientResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationResponse geolocation
) {
}
