package com.example.template.dto.response;

import lombok.Builder;

@Builder
public record RecipientResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String telegramId,
        GeolocationResponse geolocation
) {
}
