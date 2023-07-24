package com.example.notification.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${services.shortener}")
public interface ShortenerClient {

    @PostMapping("/api/v1/responses/")
    ResponseEntity<Long> create(
            @RequestBody @Valid NotificationOptionsRequest request
    );
}
