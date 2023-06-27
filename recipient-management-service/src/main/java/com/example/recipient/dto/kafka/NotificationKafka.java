package com.example.recipient.dto.kafka;

import com.example.recipient.dto.response.ClientResponse;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.model.NotificationStatus;

public record NotificationKafka(
        Long id,
        NotificationStatus status,
        RecipientResponse recipient,
        TemplateResponse template,
        ClientResponse client,
        Integer retryAttempts
) {
}
