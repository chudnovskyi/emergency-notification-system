package com.example.recipient.listener;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.exception.notification.NotificationMappingNotFoundException;
import com.example.recipient.mapper.NotificationMapper;
import com.example.recipient.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, NotificationKafka> kafkaTemplate;
    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @Value("${spring.kafka.topics.notification}")
    private String notificationTopic;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.splitter}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientListKafka recipientListKafka) {
        // Create a separate thread for each RecipientListKafka and execute them concurrently
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        NotificationSenderThread thread = new NotificationSenderThread(
                kafkaTemplate,
                notificationService,
                notificationTopic,
                recipientListKafka,
                mapper
        );

        executorService.execute(thread);
        executorService.shutdown();
    }

    private record NotificationSenderThread(
            KafkaTemplate<String, NotificationKafka> kafkaTemplate,
            NotificationService notificationService,
            String notificationTopic,
            RecipientListKafka recipientListKafka,
            NotificationMapper mapper
    ) implements Runnable {

        @Override
        public void run() {
            Long clientId = recipientListKafka.clientId();
            Long templateId = recipientListKafka.templateId();

            for (Long recipientId : recipientListKafka.recipientIds()) {
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .clientId(clientId)
                        .templateId(templateId)
                        .recipientId(recipientId)
                        .build();

                NotificationResponse notificationResponse;
                try {
                    notificationResponse = notificationService.createNotification(notificationRequest);
                } catch (EntityNotFoundException e) {
                    throw new NotificationMappingNotFoundException(e.getMessage());
                }

                NotificationKafka notificationKafka = mapper.mapToKafka(notificationResponse);
                kafkaTemplate.send(notificationTopic, notificationKafka);
            }
        }
    }
}
