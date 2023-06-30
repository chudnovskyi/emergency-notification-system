package com.example.recipient.listener;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.exception.recipient.RecipientNotFoundException;
import com.example.recipient.mapper.NotificationMapper;
import com.example.recipient.model.NotificationType;
import com.example.recipient.service.NotificationService;
import com.example.recipient.service.RecipientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.example.recipient.model.NotificationType.*;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final RecipientService recipientService;
    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @Value("${spring.kafka.topics.notifications.email}")
    private String emailTopic;

    @Value("${spring.kafka.topics.notifications.phone}")
    private String phoneTopic;

    @Value("${spring.kafka.topics.notifications.telegram}")
    private String telegramTopic;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.splitter}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientListKafka recipientListKafka) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> {
            Long clientId = recipientListKafka.clientId();
            TemplateHistoryResponse template = recipientListKafka.templateHistoryResponse();

            for (Long recipientId : recipientListKafka.recipientIds()) {
                RecipientResponse response;
                try {
                    response = recipientService.receive(clientId, recipientId);
                } catch (RecipientNotFoundException e) {
                    // TODO
                    continue;
                }

                sendNotificationByCredential(response::email, EMAIL, response, clientId, template, emailTopic);
                sendNotificationByCredential(response::phoneNumber, PHONE, response, clientId, template, phoneTopic);
                sendNotificationByCredential(response::telegramId, TELEGRAM, response, clientId, template, telegramTopic);
            }
        };

        executorService.execute(runnable);
        executorService.shutdown();
    }

    private void sendNotificationByCredential(
            Supplier<String> supplier,
            NotificationType type,
            RecipientResponse response,
            Long clientId,
            TemplateHistoryResponse template,
            String topic
    ) {
        String credential = supplier.get();
        if (credential != null) {
            NotificationResponse notificationResponse;
            try {
                notificationResponse = notificationService.createNotification(
                        buildNotificationRequest(type, credential, template),
                        clientId,
                        response.id()
                );
            } catch (EntityNotFoundException e) {
                // TODO
                return;
            }
            NotificationKafka notificationKafka = mapper.mapToKafka(notificationResponse);
            kafkaTemplate.send(topic, notificationKafka);
        }
    }

    private NotificationRequest buildNotificationRequest(
            NotificationType type,
            String credential,
            TemplateHistoryResponse template
    ) {
        return NotificationRequest.builder()
                .type(type)
                .credential(credential)
                .template(template)
                .build();
    }
}
