package com.example.template.listener;

import com.example.template.dto.kafka.TemplateRecipientKafka;
import com.example.template.mapper.RecipientIdMapper;
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
    private final RecipientIdMapper mapper;

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topics.template-update}")
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
                    recipientIdRepository.save(mapper.mapToEntity(kafka));
                }
            }
        }
    }
}
