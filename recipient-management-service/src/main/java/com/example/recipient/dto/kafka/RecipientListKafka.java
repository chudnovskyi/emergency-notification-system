package com.example.recipient.dto.kafka;

import com.example.recipient.dto.response.TemplateResponse;

import java.util.List;

public record RecipientListKafka(
        List<Long> recipientIds,
        TemplateResponse templateResponse,
        Long clientId
) {
}
