package com.example.recipient.dto.response;

import java.util.List;

public record TemplateResponse(
        Long id,
        String title,
        String content,
        String imageUrl,
        List<RecipientResponse> recipients
) {
}
