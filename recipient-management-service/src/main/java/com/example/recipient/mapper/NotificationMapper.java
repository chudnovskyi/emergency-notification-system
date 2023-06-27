package com.example.recipient.mapper;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<Notification, NotificationRequest, NotificationKafka> {

}
