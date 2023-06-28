package com.example.recipient.controller;

import com.example.recipient.dto.response.NotificationResponse;
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

    @PostMapping("/{id}/sent")
    @Operation(summary = "set Notification status as successfully sent to Recipient")
    public ResponseEntity<NotificationResponse> setNotificationAsSent(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsSent(client.getId(), id));
    }

    @PostMapping("/{id}/error")
    @Operation(summary = "set Notification status as error ")
    public ResponseEntity<NotificationResponse> setNotificationAsError(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsError(client.getId(), id));
    }

    @PostMapping("/{id}/corrupted")
    @Operation(summary = "set Notification status as impossible to sent")
    public ResponseEntity<NotificationResponse> setNotificationAsCorrupted(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsCorrupted(client.getId(), id));
    }

    @PostMapping("/{id}/resend")
    @Operation(summary = "set Notification status as waiting to resend")
    public ResponseEntity<NotificationResponse> setNotificationAsResend(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsResend(client.getId(), id));
    }
}
