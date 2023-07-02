package com.example.template.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TemplateRequest(
        @NotNull(message = "{template.title.not_null}") @Size(min = 5, max = 25, message = "{template.title.size}")
        String title,

        @NotNull(message = "{template.content.not_null}") @Size(min = 5, max = 225, message = "{template.content.size}")
        String content
) {
}
