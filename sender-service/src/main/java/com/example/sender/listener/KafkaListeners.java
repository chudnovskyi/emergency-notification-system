package com.example.sender.listener;

import com.example.sender.client.NotificationClient;
import com.example.sender.dto.kafka.NotificationKafka;
import com.example.sender.dto.response.TemplateHistoryResponse;
import com.example.sender.service.telegram.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramAlertService telegramAlertService;
    private final NotificationClient notificationClient;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.telegram}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void telegramNotificationListener(NotificationKafka notificationKafka) {
        log(notificationKafka);
        String credential = notificationKafka.credential();
        TemplateHistoryResponse template = notificationKafka.template();
        boolean isSuccessfullySent = telegramAlertService.sendMessage(credential, template);
        if (isSuccessfullySent) {
            notificationClient.setNotificationAsSent(notificationKafka.clientId(), notificationKafka.id());
        } else {
            notificationClient.setNotificationAsResending(notificationKafka.clientId(), notificationKafka.id());
        }
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.email}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void emailNotificationListener(NotificationKafka notificationKafka) {
        log(notificationKafka);
        notificationClient.setNotificationAsCorrupt(notificationKafka.clientId(), notificationKafka.id());
//         TODO: Amazon SES
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notifications.phone}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void phoneNotificationListener(NotificationKafka notificationKafka) {
        log(notificationKafka);
        notificationClient.setNotificationAsCorrupt(notificationKafka.clientId(), notificationKafka.id());
//         TODO: Twilio
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
}
