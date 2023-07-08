package com.example.notification.client;

import com.example.notification.dto.response.RecipientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${services.recipient}")
public interface RecipientClient {

    @GetMapping("/api/v1/recipients/{id}")
    ResponseEntity<RecipientResponse> receiveByClientIdAndRecipientId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    );
}
