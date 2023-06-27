package com.example.recipient.dto.request;

import lombok.Builder;

@Builder
public record NotificationRequest(
        Long recipientId,
        Long templateId,
        Long clientId,
        Integer retryAttempts
) {
}
