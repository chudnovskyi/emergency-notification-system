package com.example.notification.service;

import com.example.notification.client.TemplateClient;
import com.example.notification.dto.kafka.RecipientListKafka;
import com.example.notification.dto.request.NotificationRequest;
import com.example.notification.dto.response.NotificationResponse;
import com.example.notification.dto.response.RecipientResponse;
import com.example.notification.dto.response.TemplateHistoryResponse;
import com.example.notification.dto.response.TemplateResponse;
import com.example.notification.entity.Notification;
import com.example.notification.exception.notification.NotificationNotFoundException;
import com.example.notification.exception.template.TemplateRecipientsNotFound;
import com.example.notification.mapper.NotificationMapper;
import com.example.notification.model.NotificationStatus;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.util.CollectionUtils;
import com.example.notification.util.NodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.notification.model.NotificationStatus.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, RecipientListKafka> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final TemplateClient templateClient;
    private final MessageSourceService message;
    private final NotificationMapper mapper;
    private final NodeChecker nodeChecker;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.kafka.topics.splitter}")
    private String recipientListDistributionTopic;

    @Value("${notifications.maxRetryAttempts}")
    private Integer maxRetryAttempts;

    public String distributeNotifications(Long clientId, Long templateId) {
        TemplateResponse templateResponse = templateClient.getTemplateByClientIdAndTemplateId(clientId, templateId)
                .getBody(); // TODO: retrieve list of IDS at once, not TemplateResponse

        if (templateResponse == null) {
            return "TODO"; // TODO
        }

        List<Long> recipientIds = templateResponse.recipientIds()
                .stream()
                .map(RecipientResponse::id)
                .toList();
        if (recipientIds.size() == 0) {
            throw new TemplateRecipientsNotFound(
                    message.getProperty("template.recipients.not_found", templateId, clientId)
            );
        }

        TemplateHistoryResponse templateHistoryResponse = templateClient.createTemplateHistory(clientId, templateId)
                .getBody();

        for (List<Long> recipients : splitRecipientIds(recipientIds)) {
            kafkaTemplate.send(recipientListDistributionTopic, new RecipientListKafka(recipients, templateHistoryResponse, clientId));
        }

        return "Notification's been successfully sent!";
    }

    public NotificationResponse createNotification(NotificationRequest request) {
        return Optional.of(request)
                .map(mapper::mapToEntity)
                .map(notification -> notification.addTemplateHistory(request.template().id()))
                .map(notificationRepository::saveAndFlush)
                .map(notification -> mapper.mapToResponse(notification, templateClient))
                .orElseThrow(); // TODO
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
        return notificationRepository.findByIdAndClientId(notificationId, clientId)
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
                .map(notification -> mapper.mapToResponse(notification, templateClient))
                .orElseThrow(() -> new NotificationNotFoundException(
                        message.getProperty("notification.not_found", notificationId, clientId)
                ));
    }

    private NotificationResponse setNotificationStatus(Long clientId, Long notificationId, NotificationStatus status) {
        return notificationRepository.findByIdAndClientId(notificationId, clientId)
                .map(notification -> notification.setNotificationStatus(status))
                .map(notificationRepository::saveAndFlush)
                .map(notification -> mapper.mapToResponse(notification, templateClient))
                .orElseThrow(() -> new NotificationNotFoundException(
                        message.getProperty("notification.not_found", notificationId, clientId)
                ));
    }

    private List<List<Long>> splitRecipientIds(List<Long> list) {
        return CollectionUtils.splitList(list, nodeChecker.getAmountOfRunningNodes(applicationName));
    }
}
