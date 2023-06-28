package com.example.recipient.dto.response;

import com.example.recipient.model.NotificationStatus;

public record NotificationResponse(
        Long id,
        NotificationStatus notificationStatus,
        RecipientResponse recipient,
        TemplateResponse template,
        ClientResponse client,
        Integer retryAttempts
) {
}
