package com.example.sender.client;

import com.example.sender.dto.response.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${services.notification}")
public interface NotificationClient {

    @PostMapping(value = "/api/v1/notifications/{id}/sent")
    ResponseEntity<NotificationResponse> setNotificationAsSent(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{id}/resending")
    ResponseEntity<NotificationResponse> setNotificationAsResending(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{id}/corrupt")
    ResponseEntity<NotificationResponse> setNotificationAsCorrupt(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    );

    @PostMapping(value = "/api/v1/notifications/{id}/error")
    ResponseEntity<NotificationResponse> setNotificationAsError(
            @RequestHeader Long clientId,
            @PathVariable("id") Long notificationId
    );
}
