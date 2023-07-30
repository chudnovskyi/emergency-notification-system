package com.example.notification.client;

import com.example.notification.dto.response.UrlsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${services.shortener}")
public interface ShortenerClient {

    @PostMapping("/api/v1/responses/generate/{id}")
    ResponseEntity<UrlsResponse> generate(
            @PathVariable("id") Long responseId
    );
}
