package com.example.notification.dto.request;

import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.model.NotificationType;
import lombok.Builder;

@Builder
public record NotificationRequest(
        NotificationType type,
        String credential,
        TemplateHistoryResponse template,
        Long recipientId,
        Long clientId,
        Long urlId
) {
}
