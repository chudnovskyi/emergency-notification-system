package com.example.notification.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TemplateResponse(
        Long id,
        String title,
        String content,
        String imageUrl,
        List<RecipientResponse> recipientIds
) {
}
