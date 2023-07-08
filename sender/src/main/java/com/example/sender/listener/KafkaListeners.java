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
    private void telegramNotificationListener(NotificationKafka notificationKafka) {
        log(notificationKafka);
        if (randomlyResent(notificationKafka)) { // for Rebalancer Testing purpose
            return;
        }
        String credential = notificationKafka.credential();
        TemplateHistoryResponse template = notificationKafka.template();
        boolean isSuccessfullySent = telegramAlertService.sendMessage(credential, template);
        if (isSuccessfullySent) {
            notificationClient.setNotificationAsSent(notificationKafka.clientId(), notificationKafka.id());
        } else if (notificationKafka.retryAttempts() == maxRetryAttempts) {
            notificationClient.setNotificationAsError(notificationKafka.clientId(), notificationKafka.id());
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
        if (randomlyResent(notificationKafka)) { // for Rebalancer Testing purpose
            return;
        }
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
        if (randomlyResent(notificationKafka)) { // for Rebalancer Testing purpose
            return;
        }
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

    private boolean randomlyResent(NotificationKafka notificationKafka) {
        if (random.nextInt(100) <= 33) {
            notificationClient.setNotificationAsResending(notificationKafka.clientId(), notificationKafka.id());
            return true;
        }
        return false;
    }
}
