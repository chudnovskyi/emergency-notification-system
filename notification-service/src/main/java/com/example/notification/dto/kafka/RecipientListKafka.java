package com.example.notification.dto.kafka;

import com.example.notification.dto.response.TemplateHistoryResponse;

import java.util.List;

public record RecipientListKafka(
        List<Long> recipientIds,
        TemplateHistoryResponse templateHistoryResponse,
        Long clientId
) {
}
