package com.example.recipient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record RecipientListRequest(
        @Valid @NotEmpty(message = "{recipientList.recipients.min_size}") List<Long> recipientIds
) {
}
