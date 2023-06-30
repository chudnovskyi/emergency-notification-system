package com.example.sender.dto.kafka;

import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.model.NotificationStatus;
import com.example.sender.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationKafka(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        LocalDateTime createdAt,
        TemplateHistoryResponse template
) {
}
