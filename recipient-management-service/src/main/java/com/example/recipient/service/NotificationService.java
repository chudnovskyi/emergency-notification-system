package com.example.recipient.service;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.entity.Client;
import com.example.recipient.entity.Notification;
import com.example.recipient.exception.client.ClientNotFoundException;
import com.example.recipient.exception.recipient.RecipientNotFoundException;
import com.example.recipient.exception.template.TemplateNotFoundException;
import com.example.recipient.exception.template.TemplateRecipientsNotFound;
import com.example.recipient.mapper.NotificationMapper;
import com.example.recipient.repository.ClientRepository;
import com.example.recipient.repository.NotificationRepository;
import com.example.recipient.repository.RecipientRepository;
import com.example.recipient.repository.TemplateRepository;
import com.example.recipient.util.CollectionUtils;
import com.example.recipient.util.NodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, RecipientListKafka> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final RecipientRepository recipientRepository;
    private final TemplateRepository templateRepository;
    private final ClientRepository clientRepository;
    private final MessageSourceService message;
    private final NotificationMapper mapper;
    private final NodeChecker nodeChecker;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.kafka.topics.splitter}")
    private String notificationTopic;

    public String distributeNotifications(Client client, Long templateId) {
        List<Long> recipientIds = templateRepository.findRecipientIdsByTemplateId(templateId);

        if (recipientIds.size() == 0) {
            throw new TemplateRecipientsNotFound(
                    message.getProperty("template.recipients.not_found", templateId, client.getId())
            );
        }

        List<List<Long>> lists = splitRecipientIds(recipientIds);

        for (List<Long> list : lists) {
            kafkaTemplate.send(notificationTopic, new RecipientListKafka(list, templateId, client.getId()));
        }

        return "Notification's been successfully sent!";
    }

    public void sendNotification() {

    }

    @Transactional
    public NotificationKafka createNotification(NotificationRequest request) {
        Long templateId = request.templateId();
        Long clientId = request.clientId();
        Long recipientId = request.recipientId();
        Notification notification = Notification.builder()
                .template(templateRepository.findByIdAndClient_Id(templateId, clientId).orElseThrow(
                        () -> new TemplateNotFoundException(message.getProperty("template.not_found", templateId, clientId))
                ))
                .client(clientRepository.findById(clientId).orElseThrow(
                        () -> new ClientNotFoundException(message.getProperty("client.not_found", clientId))
                ))
                .recipient(recipientRepository.findByIdAndClient_Id(recipientId, clientId).orElseThrow(
                        () -> new RecipientNotFoundException(message.getProperty("recipient.not_found", recipientId))
                ))
                .build();

        Notification save = notificationRepository.save(notification);

        return mapper.mapToResponse(save);
    }

    private List<List<Long>> splitRecipientIds(List<Long> list) {
        return CollectionUtils.splitList(list, nodeChecker.getAmountOfRunningNodes(applicationName));
    }
}
