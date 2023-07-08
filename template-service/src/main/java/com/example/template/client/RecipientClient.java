package com.example.template.client;

import com.example.template.dto.response.RecipientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "${services.recipeint}")
public interface RecipientClient {

    @GetMapping(value = "/api/v1/recipients/{id}")
    ResponseEntity<RecipientResponse> receiveRecipientById(
            @RequestHeader Long clientId,
            @PathVariable("id") Long recipientId
    );

    @GetMapping(value = "/api/v1/recipients/template/{id}")
    ResponseEntity<List<RecipientResponse>> receiveRecipientResponseListByTemplateId(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    );
}
