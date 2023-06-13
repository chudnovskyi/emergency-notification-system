package com.example.recipient.dto.response;

public record RecipientResponse(
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationResponse geolocation
) {
}
