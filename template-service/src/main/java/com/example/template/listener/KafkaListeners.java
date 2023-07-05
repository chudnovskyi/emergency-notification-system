package com.example.template.listener;

import com.example.template.dto.kafka.TemplateRecipientKafka;
import com.example.template.entity.RecipientId;
import com.example.template.entity.Template;
import com.example.template.repository.RecipientIdRepository;
import com.example.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final RecipientIdRepository recipientIdRepository;
    private final TemplateRepository templateRepository;

    @Transactional
    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.template-update}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    public void listener(TemplateRecipientKafka kafka) {
        switch (kafka.operation()) {
            case REMOVE -> {
                templateRepository.findById(kafka.templateId())
                        .map(template -> template.removeRecipient(kafka.recipientId()))
                        .ifPresent(templateRepository::saveAndFlush);
            }
            case PERSISTS -> {
                if (!recipientIdRepository.existsByTemplateIdAndRecipientId(
                        kafka.templateId(),
                        kafka.recipientId()
                )) {
                    recipientIdRepository.save(
                            RecipientId.builder()  // TODO: mapper
                                    .recipientId(kafka.recipientId())
                                    .template(
                                            Template.builder()
                                                    .id(kafka.templateId())
                                                    .build()
                                    )
                                    .build()
                    );
                }
            }
        }
    }
}
