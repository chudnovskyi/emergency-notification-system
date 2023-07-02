package com.example.file.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String message,
        String description,
        Integer code,
        LocalDateTime timestamp
) {
}
