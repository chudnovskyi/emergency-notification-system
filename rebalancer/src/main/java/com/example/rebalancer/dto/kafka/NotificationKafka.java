package com.example.rebalancer.dto.kafka;


import com.example.rebalancer.dto.response.TemplateHistoryResponse;
import com.example.rebalancer.model.NotificationStatus;
import com.example.rebalancer.model.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;

public record NotificationKafka(
        Long id,
        NotificationType type,
        String credential,
        NotificationStatus status,
        Integer retryAttempts,
        LocalDateTime createdAt,
        TemplateHistoryResponse template,
        Long clientId,
        Map<String, String> urlOptionMap
) {
}
