package com.example.recipient.util;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class NotificationSenderThread implements Runnable {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationService notificationService;
    private final String notificationTopic;
    private final RecipientListKafka recipientListKafka;

    @Override
    public void run() {
        Long clientId = recipientListKafka.clientId();
        Long templateId = recipientListKafka.templateId();
        for (Long recipientId : recipientListKafka.recipientIds()) {
            try {
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .clientId(clientId)
                        .templateId(templateId)
                        .recipientId(recipientId)
                        .build();
                NotificationKafka notificationKafka = notificationService.createNotification(notificationRequest);
                kafkaTemplate.send(notificationTopic, notificationKafka);
            } catch (EntityNotFoundException e) {
                // TODO: error sending not
            }
        }
    }
}
