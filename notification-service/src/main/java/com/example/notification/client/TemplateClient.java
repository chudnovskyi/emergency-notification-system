package com.example.notification.client;

import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.dto.response.TemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "template-service")
public interface TemplateClient {

    @GetMapping("/api/v1/templates/{id}")
    ResponseEntity<TemplateResponse> getTemplateByClientIdAndTemplateId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );

    @PostMapping("/api/v1/templates/history/{id}")
    ResponseEntity<TemplateHistoryResponse> createTemplateHistory(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );

    @GetMapping("/api/v1/templates/history/{id}")
    ResponseEntity<TemplateHistoryResponse> getTemplateHistory(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );
}
