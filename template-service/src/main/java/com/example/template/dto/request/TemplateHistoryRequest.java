package com.example.template.dto.request;

import lombok.Builder;

@Builder
public record TemplateHistoryRequest(
        String title,
        String content,
        String imageUrl
) {
}
