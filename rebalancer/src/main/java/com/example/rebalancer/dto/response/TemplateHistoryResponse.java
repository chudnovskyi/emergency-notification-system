package com.example.rebalancer.dto.response;

public record TemplateHistoryResponse(
        Long id,
        String title,
        String content,
        String imageUrl
) {
}
