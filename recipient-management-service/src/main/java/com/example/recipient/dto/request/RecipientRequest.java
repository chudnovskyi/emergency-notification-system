package com.example.recipient.dto.request;

public record RecipientRequest(
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationRequest geolocation
) {
}
