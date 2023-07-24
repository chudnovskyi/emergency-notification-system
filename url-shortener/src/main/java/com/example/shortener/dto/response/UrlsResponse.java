package com.example.shortener.dto.response;

import java.util.Map;

public record UrlsResponse(
        Long urlId,
        Map<String, String> urlOptionMap
) {
}
