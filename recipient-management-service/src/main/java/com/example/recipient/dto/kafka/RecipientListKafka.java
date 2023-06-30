package com.example.recipient.dto.kafka;

import com.example.recipient.dto.response.TemplateHistoryResponse;

import java.util.List;

public record RecipientListKafka(
        List<Long> recipientIds,
        TemplateHistoryResponse templateHistoryResponse,
        Long clientId
) {
}
