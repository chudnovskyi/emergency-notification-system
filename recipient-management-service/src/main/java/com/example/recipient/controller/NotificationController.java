package com.example.recipient.controller;

import com.example.recipient.entity.Client;
import com.example.recipient.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{id}")
    @Operation(summary = "send a Notification to all Recipients registered for the provided Template ID")
    public ResponseEntity<String> notify(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.distributeNotifications(client, id));
    }
}
