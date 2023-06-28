package com.example.sender.dto.kafka;

import com.example.sender.dto.response.ClientResponse;
import com.example.sender.dto.response.RecipientResponse;
import com.example.sender.dto.response.TemplateResponse;
import com.example.sender.model.NotificationStatus;

public record NotificationKafka(
        Long id,
        NotificationStatus notificationStatus,
        RecipientResponse recipient,
        TemplateResponse template,
        ClientResponse client,
        Integer retryAttempts
) {
}
