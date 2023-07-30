package com.example.notification.dto.response;

import java.util.Map;

public record UrlsResponse(
        Long urlId,
        Map<String, String> urlOptionMap
) {
}
