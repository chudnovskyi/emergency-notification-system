package com.example.template.dto.response;

import lombok.Builder;

@Builder
public record TemplateHistoryResponse(
        Long id,
        String title,
        String content,
        String imageUrl
) {
}
