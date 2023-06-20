package com.example.recipient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecipientRequest(
        @Size(max = 50, message = "{recipient.name.size}")
        String name,

        @NotNull(message = "{recipient.email.not_null}") @Email(message = "{recipient.email.invalid}") @Size(max = 255, message = "{recipient.email.size}")
        String email,

        @Size(max = 20, message = "{recipient.phone.size}")
        String phoneNumber,

        @Size(max = 20, message = "{recipient.telegram.size}")
        String telegramId,

        @Valid GeolocationRequest geolocation
) {
}
