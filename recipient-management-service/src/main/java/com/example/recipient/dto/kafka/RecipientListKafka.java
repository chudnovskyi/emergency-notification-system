package com.example.recipient.dto.kafka;

import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.RecipientResponse;

import java.util.List;

public record RecipientListKafka(
        List<RecipientResponse> recipientRequests,
        TemplateRequest templateRequest
) {
}
