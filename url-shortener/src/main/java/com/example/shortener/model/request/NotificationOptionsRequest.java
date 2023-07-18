package com.example.shortener.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NotificationOptionsRequest(
        @NotEmpty @Size(max = 5) List<String> options
) {
}
