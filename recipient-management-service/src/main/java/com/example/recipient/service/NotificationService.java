package com.example.recipient.service;

import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.entity.Notification;
import com.example.recipient.exception.notification.NotificationNotFoundException;
import com.example.recipient.exception.template.TemplateRecipientsNotFound;
import com.example.recipient.mapper.NotificationMapper;
import com.example.recipient.mapper.TemplateMapper;
import com.example.recipient.model.NotificationStatus;
import com.example.recipient.repository.NotificationRepository;
import com.example.recipient.repository.TemplateHistoryRepository;
import com.example.recipient.repository.TemplateRepository;
import com.example.recipient.util.CollectionUtils;
import com.example.recipient.util.NodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.recipient.model.NotificationStatus.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, RecipientListKafka> kafkaTemplate;
    private final TemplateHistoryRepository templateHistoryRepository;
    private final NotificationRepository notificationRepository;
    private final TemplateRepository templateRepository;
    private final MessageSourceService message;
    private final NotificationMapper notificationMapper;
    private final TemplateMapper templateMapper; // TODO: extract to template service
    private final NodeChecker nodeChecker;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.kafka.topics.splitter}")
    private String notificationTopic;

    @Value("${notifications.maxRetryAttempts}")
    private Integer maxRetryAttempts;

    public String distributeNotifications(Long clientId, Long templateId) {
        List<Long> recipientIds = templateRepository.findRecipientIdsByTemplateIdAndClientId(templateId, clientId);

        if (recipientIds.size() == 0) {
            throw new TemplateRecipientsNotFound(
                    message.getProperty("template.recipients.not_found", templateId, clientId)
            );
        }

        TemplateHistoryResponse templateHistoryResponse = templateRepository.findByIdAndClient_Id(templateId, clientId) // TODO: retrieve existing if the fields repeats
                .map(templateMapper::mapToTemplateHistory)
                .map(templateHistoryRepository::saveAndFlush)
                .map(templateMapper::mapToTemplateHistoryResponse)
                .orElseThrow(); // TODO

        for (List<Long> recipients : splitRecipientIds(recipientIds)) {
            kafkaTemplate.send(notificationTopic, new RecipientListKafka(recipients, templateHistoryResponse, clientId));
        }

        return "Notification's been successfully sent!";
    }

    public NotificationResponse createNotification(NotificationRequest request) {
        return Optional.of(request)
                .map(notificationMapper::mapToEntity)
                .map(notificationRepository::saveAndFlush)
                .map(notificationMapper::mapToResponse)
                .orElseThrow();
    }

    public NotificationResponse setNotificationAsSent(Long clientId, Long notificationId) {
        return setNotificationStatus(clientId, notificationId, SENT);
    }

    public NotificationResponse setNotificationAsError(Long clientId, Long notificationId) {
        return setNotificationStatus(clientId, notificationId, ERROR);
    }

    public NotificationResponse setNotificationAsCorrupt(Long clientId, Long notificationId) {
        return setNotificationStatus(clientId, notificationId, CORRUPT);
    }

    public NotificationResponse setNotificationAsResending(Long clientId, Long notificationId) {
        return notificationRepository.findByIdAndClient_Id(notificationId, clientId)
                .map(Notification::incrementRetryAttempts)
                .map(notification -> {
                    if (notification.getRetryAttempts() >= maxRetryAttempts) {
                        notification.setNotificationStatus(ERROR);
                    } else {
                        notification.setNotificationStatus(RESENDING);
                    }
                    return notification;
                })
                .map(notificationRepository::saveAndFlush)
                .map(notificationMapper::mapToResponse)
                .orElseThrow(() -> new NotificationNotFoundException(
                        message.getProperty("notification.not_found", notificationId, clientId)
                ));
    }

    private NotificationResponse setNotificationStatus(Long clientId, Long notificationId, NotificationStatus status) {
        return notificationRepository.findByIdAndClient_Id(notificationId, clientId)
                .map(notification -> notification.setNotificationStatus(status))
                .map(notificationRepository::saveAndFlush)
                .map(notificationMapper::mapToResponse)
                .orElseThrow(() -> new NotificationNotFoundException(
                        message.getProperty("notification.not_found", notificationId, clientId)
                ));
    }

    private List<List<Long>> splitRecipientIds(List<Long> list) {
        return CollectionUtils.splitList(list, nodeChecker.getAmountOfRunningNodes(applicationName));
    }
}
