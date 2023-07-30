package com.example.notification.dto.kafka;

import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.model.NotificationStatus;
import com.example.notification.model.NotificationType;

import java.util.Map;

public record NotificationKafka(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        TemplateHistoryResponse template,
        Long clientId,
        Map<String, String> urlOptionMap
) {
}
