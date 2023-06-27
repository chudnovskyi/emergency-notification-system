package com.example.recipient.listener;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.exception.notification.NotificationMappingNotFoundException;
import com.example.recipient.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationService notificationService;

    @Value("${spring.kafka.topics.notification}")
    private String notificationTopic;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.splitter}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientListKafka recipientListKafka) {
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
                throw new NotificationMappingNotFoundException(e.getMessage());
            }
        }
    }
}
