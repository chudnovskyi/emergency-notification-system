package com.example.notification.controller;

import com.example.notification.dto.kafka.NotificationKafka;
import com.example.notification.dto.response.NotificationHistoryResponse;
import com.example.notification.dto.response.NotificationResponse;
import com.example.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsSent(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsSent(clientId, notificationId));
    }

    @PostMapping("/{id}/error")
    @Operation(summary = "set Notification status as error")
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsError(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsError(clientId, notificationId));
    }

    @PostMapping("/{id}/corrupt")
    @Operation(summary = "set Notification status as impossible to sent")
    public ResponseEntity<NotificationHistoryResponse> setNotificationAsCorrupt(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsCorrupt(clientId, notificationId));
    }

    @PostMapping("/{id}/resending")
    @Operation(summary = "set Notification status as waiting to be resend")
    public ResponseEntity<NotificationResponse> setNotificationAsResending(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    ) {
        return ResponseEntity.status(OK).body(notificationService.setNotificationAsResending(clientId, notificationId));
    }

    @GetMapping("/")
    @Operation(summary = "FOR REBALANCER: get Resending/Pending/New Notifications (set Pending status)")
    public ResponseEntity<List<NotificationKafka>> getNotificationsForRebalancing(
            @RequestHeader Long clientId,
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingSec,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newSec,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    ) {
        return ResponseEntity.status(OK).body(
                notificationService.getNotificationsForRebalancing(clientId, pendingSec, newSec, size)
        );
    }
}
