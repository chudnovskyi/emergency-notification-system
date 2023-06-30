package com.example.recipient.mapper;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.entity.Notification;
import com.example.recipient.entity.Recipient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
                Recipient.class,
                Client.class
        }
)
public interface NotificationMapper extends EntityMapper<Notification, NotificationRequest, NotificationResponse> {

    NotificationKafka mapToKafka(NotificationResponse notificationResponse);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "retryAttempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "client", expression = "java(Client.builder().id(request.clientId()).build())")
    @Mapping(target = "recipient", expression = "java(Recipient.builder().id(request.recipientId()).build())")
    Notification mapToEntity(NotificationRequest request);
}
