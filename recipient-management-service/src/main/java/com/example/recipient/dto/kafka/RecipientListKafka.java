package com.example.recipient.dto.kafka;

import java.util.List;

public record RecipientListKafka(
        List<Long> recipientIds,
        Long templateId,
        Long clientId
) {
}
