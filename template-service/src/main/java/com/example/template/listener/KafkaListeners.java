package com.example.template.listener;

import com.example.template.dto.kafka.TemplateRecipientKafka;
import com.example.template.entity.RecipientId;
import com.example.template.entity.Template;
import com.example.template.repository.RecipientIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final RecipientIdRepository recipientIdRepository;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.template-update}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(TemplateRecipientKafka templateRecipientKafka) {
        RecipientId recipientId = RecipientId.builder()  // TODO: mapper
                .recipientId(templateRecipientKafka.recipientId())
                .template(
                        Template.builder()
                                .id(templateRecipientKafka.templateId())
                                .build()
                )
                .build();

        switch (templateRecipientKafka.operation()) {
            case REMOVE -> recipientIdRepository.delete(recipientId);
            case PERSISTS -> {
                if (!recipientIdRepository.existsByTemplateIdAndRecipientId(
                        templateRecipientKafka.templateId(),
                        templateRecipientKafka.recipientId()
                )) {
                    recipientIdRepository.save(recipientId);
                }
            }
        }
    }
}
