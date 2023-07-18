package com.example.template.client;

import com.example.template.dto.request.NotificationOptionsRequest;
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
