package com.example.sender.dto.kafka;

import com.example.sender.dto.request.TemplateRequest;
import com.example.sender.dto.response.RecipientResponse;

import java.util.List;

public record RecipientListKafka(
        List<RecipientResponse> recipientRequests,
        TemplateRequest templateRequest
) {
}
