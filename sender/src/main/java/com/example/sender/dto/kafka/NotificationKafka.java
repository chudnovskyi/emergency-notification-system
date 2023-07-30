package com.example.sender.dto.kafka;

import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.model.NotificationStatus;
import com.example.sender.model.NotificationType;

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
