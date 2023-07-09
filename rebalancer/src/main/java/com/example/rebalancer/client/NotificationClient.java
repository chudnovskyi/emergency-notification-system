package com.example.rebalancer.client;

import com.example.rebalancer.dto.kafka.NotificationKafka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("${services.notification}")
public interface NotificationClient {

    @GetMapping("/api/v1/notifications/")
    ResponseEntity<List<NotificationKafka>> getNotificationsForRebalancing(
            @RequestParam(name = "pending", required = false, defaultValue = "10") Long pendingSec,
            @RequestParam(name = "new", required = false, defaultValue = "10") Long newSec,
            @RequestParam(name = "size", required = false, defaultValue = "20") Integer size
    );
}
