package com.example.template.dto.request;

import java.util.List;

public record NotificationOptionsRequest(
        List<String> options
) {
}
