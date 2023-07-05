package com.example.notification.dto.request;

import lombok.Builder;

@Builder
public record GeolocationRequest(
        double latitude,
        double longitude
) {
}
