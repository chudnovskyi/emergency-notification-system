package com.example.sender.listener;

import com.example.sender.dto.kafka.NotificationKafka;
import com.example.sender.dto.response.RecipientResponse;
import com.example.sender.dto.response.TemplateResponse;
import com.example.sender.services.telegram.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramAlertService telegramAlertService;

    private final ThreadPoolTaskExecutor emailSenderExecutor;
    private final ThreadPoolTaskExecutor phoneSenderExecutor;
    private final ThreadPoolTaskExecutor telegramSenderExecutor;
    public int i = 0;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(NotificationKafka notificationKafka) {
        TemplateResponse message = notificationKafka.template();
        RecipientResponse recipientResponse = notificationKafka.recipient();

        if (recipientResponse.email() != null) {
            emailSenderExecutor.execute(() -> {
                //   TODO: send email
            });
        }
        if (recipientResponse.phoneNumber() != null) {
            phoneSenderExecutor.execute(() -> {
                //   TODO: send SMS
            });
        }
        if (recipientResponse.telegramId() != null) {
            telegramSenderExecutor.execute(() -> {
                sendTelegramNotification(recipientResponse.telegramId(), message.content());
            });
        }
        System.out.println("finish: " + i++);
    }

    private void sendTelegramNotification(String telegramId, String content) {
        boolean isSent = telegramAlertService.sendMessage(telegramId, content);
        System.out.println(telegramId + ":" + isSent);
        if (isSent) {
            // TODO: rebalancer: update status as Successful
        } else {
            // TODO: rebalancer: update status as NotFound
        }
    }
}
