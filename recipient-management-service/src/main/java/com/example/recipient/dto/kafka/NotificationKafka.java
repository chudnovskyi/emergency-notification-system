package com.example.recipient.dto.kafka;

import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.model.NotificationStatus;
import com.example.recipient.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationKafka(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        LocalDateTime createdAt,
        TemplateHistoryResponse template,
        Long clientId
) {
}
