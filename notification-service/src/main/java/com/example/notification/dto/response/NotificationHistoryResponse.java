package com.example.notification.dto.response;

import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationHistoryResponse(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        LocalDateTime createdAt,
        LocalDateTime executedAt,
        TemplateHistoryResponse template,
        Long clientId
) {
}
