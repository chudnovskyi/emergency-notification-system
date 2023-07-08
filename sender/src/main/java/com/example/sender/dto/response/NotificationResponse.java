package com.example.sender.dto.response;

import com.example.sender.model.NotificationStatus;
import com.example.sender.model.NotificationType;

import java.time.LocalDateTime;

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
