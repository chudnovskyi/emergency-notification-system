package com.example.recipient.listener;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.exception.notification.NotificationMappingNotFoundException;
import com.example.recipient.service.NotificationService;
import com.example.recipient.util.NotificationSenderThread;
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
        executorService.execute(new NotificationSenderThread(kafkaTemplate, notificationService, notificationTopic, recipientListKafka));
        executorService.shutdown();
    }
}
