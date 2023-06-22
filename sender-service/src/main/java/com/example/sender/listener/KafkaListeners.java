package com.example.sender.listener;

import com.example.sender.dto.kafka.RecipientKafka;
import com.example.sender.dto.response.RecipientResponse;
import com.example.sender.dto.response.TemplateResponse;
import com.example.sender.services.telegram.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramAlertService telegramAlertService;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientKafka recipientKafka) {
        TemplateResponse message = recipientKafka.templateResponse();
        RecipientResponse recipientResponse = recipientKafka.recipientResponse();

        if (recipientResponse.email() != null) {
            // TODO: send email
        }
        if (recipientResponse.phoneNumber() != null) {
            // TODO: send
        }
        if (recipientResponse.telegramId() != null) {
            sendTelegramNotification(recipientResponse.telegramId(), message.content());
        }
    }

    private void sendTelegramNotification(String telegramId, String content) {
        boolean isSent = telegramAlertService.sendMessage(telegramId, content);
        System.out.println(telegramId + ":" + isSent);
        if (!isSent) {
            // TODO: rebalancer: update status as NotFound
        } else {
            // TODO: rebalancer: update status as Successful
        }
    }
}
