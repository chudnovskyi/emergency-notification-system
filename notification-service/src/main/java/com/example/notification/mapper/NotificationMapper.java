package com.example.notification.mapper;

import com.example.notification.client.TemplateClient;
import com.example.notification.dto.kafka.NotificationKafka;
import com.example.notification.dto.request.NotificationRequest;
import com.example.notification.dto.response.NotificationHistoryResponse;
import com.example.notification.dto.response.NotificationResponse;
import com.example.notification.entity.Notification;
import com.example.notification.entity.NotificationHistory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<Notification, NotificationRequest, NotificationResponse> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "retryAttempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "templateHistoryId", ignore = true)
    Notification mapToEntity(NotificationRequest request);

    @Mapping(target = "template", expression = "java(templateClient.getTemplateHistory(notification.getClientId(), notification.getTemplateHistoryId()).getBody())")
    NotificationResponse mapToResponse(Notification notification, @Context TemplateClient templateClient);

    @Mapping(target = "template", expression = "java(templateClient.getTemplateHistory(notification.getClientId(), notification.getTemplateHistoryId()).getBody())")
    NotificationKafka mapToKafka(Notification notification, @Context TemplateClient templateClient);

    NotificationKafka mapToKafka(NotificationResponse notificationResponse);

    @Mapping(target = "id", ignore = true)
    NotificationHistory mapToHistory(Notification notification);

    NotificationHistoryResponse mapToResponse(NotificationHistory notificationHistory);
}
