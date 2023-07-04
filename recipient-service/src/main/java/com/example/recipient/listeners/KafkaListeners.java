package com.example.recipient.listeners;

import com.example.recipient.dto.kafka.TemplateRecipientKafka;
import com.example.recipient.entity.Recipient;
import com.example.recipient.entity.TemplateId;
import com.example.recipient.repository.RecipientRepository;
import com.example.recipient.repository.TemplateIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TemplateIdRepository templateIdRepository;
    private final RecipientRepository recipientRepository;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.recipient-update}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(TemplateRecipientKafka templateRecipientKafka) {
        TemplateId templateId = TemplateId.builder()  // TODO: mapper
                .recipient(
                        Recipient.builder()
                                .id(templateRecipientKafka.recipientId())
                                .build()
                )
                .templateId(templateRecipientKafka.templateId())
                .build();

        switch (templateRecipientKafka.operation()) {
            case REMOVE -> {
                recipientRepository.findById(templateRecipientKafka.recipientId())
                        .map(recipient -> recipient.removeTemplate(templateRecipientKafka.templateId()))
                        .ifPresent(recipientRepository::saveAndFlush);
            }
            case PERSISTS -> {
                if (!templateIdRepository.existsByTemplateIdAndRecipientId(
                        templateRecipientKafka.templateId(),
                        templateRecipientKafka.recipientId()
                )) {
                    templateIdRepository.save(templateId);
                }
            }
        }
    }
}
