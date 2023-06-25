package com.example.sender.listener;

import com.example.sender.dto.kafka.RecipientKafka;
import com.example.sender.dto.response.RecipientResponse;
import com.example.sender.dto.response.TemplateResponse;
import com.example.sender.services.telegram.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TelegramAlertService telegramAlertService;
    private final ExecutorService emailExecutor = Executors.newFixedThreadPool(4);
    private final ExecutorService phoneExecutor = Executors.newFixedThreadPool(4);
    private final ExecutorService telegramExecutor = Executors.newFixedThreadPool(4);

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientKafka recipientKafka) {
        TemplateResponse message = recipientKafka.templateResponse();
        RecipientResponse recipientResponse = recipientKafka.recipientResponse();

        if (recipientResponse.email() != null) {
            emailExecutor.execute(() -> {
//                 TODO: send email
            });
        }
        if (recipientResponse.phoneNumber() != null) {
            phoneExecutor.execute(() -> {
//                 TODO: send
            });
        }
        if (recipientResponse.telegramId() != null) {
            telegramExecutor.execute(() -> {
                sendTelegramNotification(recipientResponse.telegramId(), message.content());
            });
        }
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
