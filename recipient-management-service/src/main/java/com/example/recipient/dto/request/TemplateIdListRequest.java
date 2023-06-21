package com.example.recipient.dto.request;

import java.util.List;

public record TemplateIdListRequest(
        List<Long> recipientIds,
        Long templateId
) {
}
