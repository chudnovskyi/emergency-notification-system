package com.example.sender.listener;

import com.example.sender.client.NotificationClient;
import com.example.sender.dto.kafka.NotificationKafka;
import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.service.telegram.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramAlertService telegramAlertService;
    private final NotificationClient notificationClient;
    private final Random random = new Random();

    @Value("${notification.maxRetryAttempts}")
    private int maxRetryAttempts;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.telegram}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void telegramNotificationListener(NotificationKafka notification) {
        log(notification);
        Long clientId = notification.clientId();
        Long notificationId = notification.id();
        if (notification.retryAttempts() >= maxRetryAttempts) {
            notificationClient.setNotificationAsError(clientId, notificationId);
        } else if (random()) {
            notificationClient.setNotificationAsResending(clientId, notificationId);
        } else {
            String credential = notification.credential();
            TemplateHistoryResponse template = notification.template();
            boolean isSent = telegramAlertService.sendMessage(credential, template);
            if (isSent) {
                notificationClient.setNotificationAsSent(clientId, notificationId);
            } else {
                notificationClient.setNotificationAsResending(clientId, notificationId);
            }
        }
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.email}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void emailNotificationListener(NotificationKafka notification) {
        log(notification);
        Long clientId = notification.clientId();
        Long notificationId = notification.id();
        if (notification.retryAttempts() >= maxRetryAttempts) {
            notificationClient.setNotificationAsError(clientId, notificationId);
        } else if (random()) {
            notificationClient.setNotificationAsResending(clientId, notificationId);
        } else {
            notificationClient.setNotificationAsCorrupt(clientId, notificationId);
//         TODO: Amazon SES
        }
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.phone}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void phoneNotificationListener(NotificationKafka notification) {
        log(notification);
        Long clientId = notification.clientId();
        Long notificationId = notification.id();
        if (notification.retryAttempts() >= maxRetryAttempts) {
            notificationClient.setNotificationAsError(clientId, notificationId);
        } else if (random()) {
            notificationClient.setNotificationAsResending(clientId, notificationId);
        } else {
            notificationClient.setNotificationAsCorrupt(clientId, notificationId);
//         TODO: Twilio
        }
    }

    private void log(NotificationKafka notificationKafka) { // TODO: AOP logging
        log.info(
                "Sending {} notification to `{}`, status={}, retryAttempts={}",
                notificationKafka.type(),
                notificationKafka.credential(),
                notificationKafka.status(),
                notificationKafka.retryAttempts()
        );
    }

    private boolean random() {
        return random.nextInt(100) <= 33;
    }
}
