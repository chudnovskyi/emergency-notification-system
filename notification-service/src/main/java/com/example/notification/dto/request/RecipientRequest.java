package com.example.notification.dto.request;

import lombok.Builder;

@Builder
public record RecipientRequest(
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationRequest geolocation
) {
}
