package com.example.recipient.dto.request;

import java.util.List;

public record RecipientListRequest(
        List<Long> recipientIds,
        Long templateId
) {
}
