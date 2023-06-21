package com.example.recipient.dto.kafka;

import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.dto.response.RecipientResponse;

public record RecipientKafka(
        RecipientResponse recipientResponse,
        TemplateResponse templateResponse
) {
}
