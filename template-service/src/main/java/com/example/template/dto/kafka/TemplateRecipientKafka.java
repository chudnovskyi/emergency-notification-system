package com.example.template.dto.kafka;

import lombok.Builder;

@Builder
public record TemplateRecipientKafka(
        Long recipientId,
        Long templateId,
        Operation operation
) {
}
