package com.example.sender.dto.kafka;


import com.example.sender.dto.response.TemplateResponse;
import com.example.sender.dto.response.RecipientResponse;

public record RecipientKafka(
        RecipientResponse recipientResponse,
        TemplateResponse templateResponse
) {
}
