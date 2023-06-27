package com.example.recipient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RecipientListRequest(
        @Valid @NotEmpty(message = "{recipientList.recipients.min_size}") List<Long> recipientIds
) {
}
