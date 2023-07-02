package com.example.notification.dto.kafka;

import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.model.NotificationType;
import com.example.notification.model.NotificationStatus;

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
