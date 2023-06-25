package com.example.recipient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RecipientListRequest(
        @Valid @NotEmpty(message = "{recipientList.recipients.min_size}") List<Long> recipientIds,
        @NotNull(message = "{recipientList.templateId.not_null}") Long templateId
) {
}
