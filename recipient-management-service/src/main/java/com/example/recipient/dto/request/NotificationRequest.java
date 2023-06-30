package com.example.recipient.dto.request;

import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.model.NotificationType;
import lombok.Builder;

@Builder
public record NotificationRequest(
        NotificationType type,
        String credential,
        TemplateHistoryResponse template,
        Long recipientId,
        Long clientId
) {
}
