package com.example.file.client;

import com.example.file.dto.request.RecipientRequest;
import com.example.file.dto.response.RecipientResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "${services.recipient}")
public interface RecipientClient {

    @PostMapping("/api/v1/recipients/")
    ResponseEntity<RecipientResponse> register(
            @RequestHeader Long clientId,
            @RequestBody @Valid RecipientRequest request
    );

    @GetMapping("/api/v1/recipients/")
    ResponseEntity<List<RecipientResponse>> receiveByClientId(
            @RequestHeader Long clientId
    );
}
