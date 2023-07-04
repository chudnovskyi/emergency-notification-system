package com.example.recipient.listeners;

import com.example.recipient.dto.kafka.Operation;
import com.example.recipient.dto.kafka.TemplateRecipientKafka;
import com.example.recipient.entity.TemplateId;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import static com.example.recipient.dto.kafka.Operation.PERSISTS;
import static com.example.recipient.dto.kafka.Operation.REMOVE;

@RequiredArgsConstructor
public class TemplateIdListener {

    private final KafkaTemplate<String, TemplateRecipientKafka> kafkaTemplate;

    @Value("${spring.kafka.topics.template-update}")
    private String recipientUpdateTopic;

    @PostRemove
    public void postRemove(TemplateId templateId) {
        sendKafkaEvent(templateId, REMOVE);
    }

    @PostPersist
    public void postPersist(TemplateId templateId) {
        sendKafkaEvent(templateId, PERSISTS);
    }

    private void sendKafkaEvent(TemplateId templateId, Operation operation) {
        kafkaTemplate.send(
                recipientUpdateTopic,
                TemplateRecipientKafka.builder()
                        .recipientId(templateId.getRecipient().getId())
                        .templateId(templateId.getTemplateId())
                        .operation(operation)
                        .build()
        );
    }
}
