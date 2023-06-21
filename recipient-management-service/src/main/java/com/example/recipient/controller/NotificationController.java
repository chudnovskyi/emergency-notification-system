package com.example.recipient.controller;

import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.entity.Client;
import com.example.recipient.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/")
    public ResponseEntity<String> publish(
            @AuthenticationPrincipal Client client,
            @RequestBody RecipientListRequest request
    ) {
        return ResponseEntity.status(OK).body(notificationService.distributeRecipients(client, request));
    }
}
