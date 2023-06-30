package com.example.recipient.dto.response;

import com.example.recipient.model.NotificationStatus;
import com.example.recipient.model.NotificationType;
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
        TemplateHistoryResponse template
) {
}
