package com.example.notification.dto.kafka;

import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;

public record NotificationKafka(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        TemplateHistoryResponse template,
        Long clientId
) {
}
