package com.example.recipient.mapper;

import com.example.recipient.dto.kafka.NotificationKafka;
import com.example.recipient.dto.request.NotificationRequest;
import com.example.recipient.dto.response.NotificationResponse;
import com.example.recipient.dto.response.NotificationResponse.NotificationResponseBuilder;
import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.dto.response.TemplateHistoryResponse.TemplateHistoryResponseBuilder;
import com.example.recipient.entity.Notification;
import com.example.recipient.entity.Notification.NotificationBuilder;
import com.example.recipient.entity.Recipient;
import com.example.recipient.entity.TemplateHistory;
import com.example.recipient.entity.TemplateHistory.TemplateHistoryBuilder;
import com.example.recipient.model.NotificationStatus;
import com.example.recipient.model.NotificationType;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-02T02:54:40+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification update(NotificationRequest request, Notification entity) {
        if ( request == null ) {
            return null;
        }

        entity.setClientId( request.clientId() );
        entity.setType( request.type() );
        entity.setCredential( request.credential() );
        if ( request.template() != null ) {
            if ( entity.getTemplate() == null ) {
                entity.setTemplate( new TemplateHistory() );
            }
            templateHistoryResponseToTemplateHistory( request.template(), entity.getTemplate() );
        }
        else {
            entity.setTemplate( null );
        }

        return entity;
    }

    @Override
    public NotificationKafka mapToKafka(NotificationResponse notificationResponse) {
        if ( notificationResponse == null ) {
            return null;
        }

        Long id = null;
        NotificationType type = null;
        String credential = null;
        NotificationStatus status = null;
        Integer retryAttempts = null;
        LocalDateTime createdAt = null;
        TemplateHistoryResponse template = null;
        Long clientId = null;

        id = notificationResponse.id();
        type = notificationResponse.type();
        credential = notificationResponse.credential();
        status = notificationResponse.status();
        retryAttempts = notificationResponse.retryAttempts();
        createdAt = notificationResponse.createdAt();
        template = notificationResponse.template();
        clientId = notificationResponse.clientId();

        NotificationKafka notificationKafka = new NotificationKafka( id, type, credential, status, retryAttempts, createdAt, template, clientId );

        return notificationKafka;
    }

    @Override
    public Notification mapToEntity(NotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        NotificationBuilder notification = Notification.builder();

        notification.clientId( request.clientId() );
        notification.type( request.type() );
        notification.credential( request.credential() );
        notification.template( templateHistoryResponseToTemplateHistory1( request.template() ) );

        notification.recipient( Recipient.builder().id(request.recipientId()).build() );

        return notification.build();
    }

    @Override
    public NotificationResponse mapToResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponseBuilder notificationResponse = NotificationResponse.builder();

        notificationResponse.id( notification.getId() );
        notificationResponse.type( notification.getType() );
        notificationResponse.credential( notification.getCredential() );
        notificationResponse.status( notification.getStatus() );
        notificationResponse.retryAttempts( notification.getRetryAttempts() );
        notificationResponse.createdAt( notification.getCreatedAt() );
        notificationResponse.template( templateHistoryToTemplateHistoryResponse( notification.getTemplate() ) );

        notificationResponse.clientId( notification.getClient().getId() );

        return notificationResponse.build();
    }

    protected void templateHistoryResponseToTemplateHistory(TemplateHistoryResponse templateHistoryResponse, TemplateHistory mappingTarget) {
        if ( templateHistoryResponse == null ) {
            return;
        }

        mappingTarget.setId( templateHistoryResponse.id() );
        mappingTarget.setTitle( templateHistoryResponse.title() );
        mappingTarget.setContent( templateHistoryResponse.content() );
        mappingTarget.setImageUrl( templateHistoryResponse.imageUrl() );
    }

    protected TemplateHistory templateHistoryResponseToTemplateHistory1(TemplateHistoryResponse templateHistoryResponse) {
        if ( templateHistoryResponse == null ) {
            return null;
        }

        TemplateHistoryBuilder templateHistory = TemplateHistory.builder();

        templateHistory.id( templateHistoryResponse.id() );
        templateHistory.title( templateHistoryResponse.title() );
        templateHistory.content( templateHistoryResponse.content() );
        templateHistory.imageUrl( templateHistoryResponse.imageUrl() );

        return templateHistory.build();
    }

    protected TemplateHistoryResponse templateHistoryToTemplateHistoryResponse(TemplateHistory templateHistory) {
        if ( templateHistory == null ) {
            return null;
        }

        TemplateHistoryResponseBuilder templateHistoryResponse = TemplateHistoryResponse.builder();

        templateHistoryResponse.id( templateHistory.getId() );
        templateHistoryResponse.title( templateHistory.getTitle() );
        templateHistoryResponse.content( templateHistory.getContent() );
        templateHistoryResponse.imageUrl( templateHistory.getImageUrl() );

        return templateHistoryResponse.build();
    }
}
