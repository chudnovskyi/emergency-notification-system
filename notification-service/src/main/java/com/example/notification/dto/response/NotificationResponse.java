package com.example.notification.dto.response;

import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
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
