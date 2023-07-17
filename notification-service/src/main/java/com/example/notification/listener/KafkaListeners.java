package com.example.notification.listener;

import com.example.notification.client.RecipientClient;
import com.example.notification.dto.kafka.NotificationKafka;
import com.example.notification.dto.kafka.RecipientListKafka;
import com.example.notification.dto.request.NotificationRequest;
import com.example.notification.dto.response.NotificationResponse;
import com.example.notification.dto.response.RecipientResponse;
import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.mapper.NotificationMapper;
import com.example.notification.model.NotificationType;
import com.example.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static com.example.notification.model.NotificationType.*;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationService notificationService;
    private final RecipientClient recipientClient;
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
    private void listener(RecipientListKafka recipientListKafka) { // TODO: @Async
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = () -> {
            Long clientId = recipientListKafka.clientId();
            TemplateHistoryResponse template = recipientListKafka.templateHistoryResponse();

            for (Long recipientId : recipientListKafka.recipientIds()) {
                RecipientResponse response;
                try {
                    response = recipientClient.receiveByClientIdAndRecipientId(clientId, recipientId)
                            .getBody();
                } catch (RuntimeException e) {
                    // TODO
                    continue;
                }

                if (response == null) {
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
            RecipientResponse recipientResponse,
            Long clientId,
            TemplateHistoryResponse template,
            String topic
    ) {
        String credential = supplier.get();
        if (credential != null) {
            Long notificationId;
            try {
                notificationId = notificationService.createNotification( // TODO: mapper
                        NotificationRequest.builder()
                                .type(type)
                                .credential(credential)
                                .template(template)
                                .recipientId(recipientResponse.id())
                                .clientId(clientId)
                                .build()
                ).id();
            } catch (EntityNotFoundException e) {
                // TODO
                return;
            }
            NotificationResponse response = notificationService.setNotificationAsPending(clientId, notificationId);
            NotificationKafka notificationKafka = mapper.mapToKafka(response);
            kafkaTemplate.send(topic, notificationKafka);
        }
    }
}
