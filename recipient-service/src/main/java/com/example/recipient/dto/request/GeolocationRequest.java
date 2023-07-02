package com.example.recipient.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;

@Builder
public record GeolocationRequest(
        @DecimalMin(value = "-90", message = "{geo.latitude.min}") @DecimalMax(value = "90", message = "{geo.latitude.max}")
        double latitude,

        @DecimalMin(value = "-180", message = "{geo.longitude.min}") @DecimalMax(value = "180", message = "{geo.longitude.max}")
        double longitude
) {
}
