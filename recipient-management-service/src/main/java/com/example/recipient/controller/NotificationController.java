package com.example.recipient.controller;

import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{id}")
    @Operation(summary = "send a Notification to all Recipients registered for the provided Template ID")
    public ResponseEntity<String> notify(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.distributeNotifications(clientId, notificationId));
    }

    @PostMapping("/{id}/sent")
    @Operation(summary = "set Notification status as successfully sent to Recipient")
    public ResponseEntity<NotificationResponse> setNotificationAsSent(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsSent(clientId, notificationId));
    }

    @PostMapping("/{id}/error")
    @Operation(summary = "set Notification status as error ")
    public ResponseEntity<NotificationResponse> setNotificationAsError(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsError(client.getId(), id));
    }

    @PostMapping("/{id}/corrupt")
    @Operation(summary = "set Notification status as impossible to sent")
    public ResponseEntity<NotificationResponse> setNotificationAsCorrupt(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsCorrupt(client.getId(), id));
    }

    @PostMapping("/{id}/resending")
    @Operation(summary = "set Notification status as waiting to be resend")
    public ResponseEntity<NotificationResponse> setNotificationAsResending(
            @AuthenticationPrincipal Client client,
            @PathVariable Long id
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsResending(client.getId(), id));
    }
}
