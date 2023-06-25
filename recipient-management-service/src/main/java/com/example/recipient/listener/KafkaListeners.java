package com.example.recipient.listener;

import com.example.recipient.dto.kafka.RecipientKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.exception.recipient.RecipientNotFoundException;
import com.example.recipient.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final RecipientService recipientService;
    private final KafkaTemplate<String, RecipientKafka> kafkaTemplate;

    @Value("${spring.kafka.topics.notification}")
    private String notificationTopic;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.splitter}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientListKafka recipientListKafka) {
        Long clientId = recipientListKafka.clientId();
        TemplateResponse templateResponse = recipientListKafka.templateResponse();
        for (Long recipientId : recipientListKafka.recipientIds()) {
            try {
                RecipientResponse recipient = recipientService.receive(clientId, recipientId);
                kafkaTemplate.send(notificationTopic, new RecipientKafka(recipient, templateResponse));
            } catch (RecipientNotFoundException e) {
                // TODO: sender wasn't not found (for further re-balancing)
            }
        }
    }
}
