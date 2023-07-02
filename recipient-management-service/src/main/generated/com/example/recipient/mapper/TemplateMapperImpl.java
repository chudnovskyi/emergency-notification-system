package com.example.recipient.mapper;

import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.GeolocationResponse;
import com.example.recipient.dto.response.GeolocationResponse.GeolocationResponseBuilder;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.dto.response.RecipientResponse.RecipientResponseBuilder;
import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.dto.response.TemplateHistoryResponse.TemplateHistoryResponseBuilder;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.dto.response.TemplateResponse.TemplateResponseBuilder;
import com.example.recipient.entity.Geolocation;
import com.example.recipient.entity.Recipient;
import com.example.recipient.entity.Template;
import com.example.recipient.entity.Template.TemplateBuilder;
import com.example.recipient.entity.TemplateHistory;
import com.example.recipient.entity.TemplateHistory.TemplateHistoryBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-02T02:54:40+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class TemplateMapperImpl implements TemplateMapper {

    @Override
    public Template mapToEntity(TemplateRequest request) {
        if ( request == null ) {
            return null;
        }

        TemplateBuilder template = Template.builder();

        template.title( request.title() );
        template.content( request.content() );

        return template.build();
    }

    @Override
    public TemplateResponse mapToResponse(Template entity) {
        if ( entity == null ) {
            return null;
        }

        TemplateResponseBuilder templateResponse = TemplateResponse.builder();

        templateResponse.id( entity.getId() );
        templateResponse.title( entity.getTitle() );
        templateResponse.content( entity.getContent() );
        templateResponse.imageUrl( entity.getImageUrl() );
        templateResponse.recipients( recipientListToRecipientResponseList( entity.getRecipients() ) );

        return templateResponse.build();
    }

    @Override
    public Template update(TemplateRequest request, Template entity) {
        if ( request == null ) {
            return null;
        }

        entity.setTitle( request.title() );
        entity.setContent( request.content() );

        return entity;
    }

    @Override
    public TemplateHistory mapToTemplateHistory(Template template) {
        if ( template == null ) {
            return null;
        }

        TemplateHistoryBuilder templateHistory = TemplateHistory.builder();

        templateHistory.id( template.getId() );
        templateHistory.title( template.getTitle() );
        templateHistory.content( template.getContent() );
        templateHistory.imageUrl( template.getImageUrl() );

        return templateHistory.build();
    }

    @Override
    public TemplateHistoryResponse mapToTemplateHistoryResponse(TemplateHistory templateHistory) {
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

    protected GeolocationResponse geolocationToGeolocationResponse(Geolocation geolocation) {
        if ( geolocation == null ) {
            return null;
        }

        GeolocationResponseBuilder geolocationResponse = GeolocationResponse.builder();

        geolocationResponse.latitude( geolocation.getLatitude() );
        geolocationResponse.longitude( geolocation.getLongitude() );

        return geolocationResponse.build();
    }

    protected RecipientResponse recipientToRecipientResponse(Recipient recipient) {
        if ( recipient == null ) {
            return null;
        }

        RecipientResponseBuilder recipientResponse = RecipientResponse.builder();

        recipientResponse.id( recipient.getId() );
        recipientResponse.name( recipient.getName() );
        recipientResponse.email( recipient.getEmail() );
        recipientResponse.phoneNumber( recipient.getPhoneNumber() );
        recipientResponse.telegramId( recipient.getTelegramId() );
        recipientResponse.geolocation( geolocationToGeolocationResponse( recipient.getGeolocation() ) );

        return recipientResponse.build();
    }

    protected List<RecipientResponse> recipientListToRecipientResponseList(List<Recipient> list) {
        if ( list == null ) {
            return null;
        }

        List<RecipientResponse> list1 = new ArrayList<RecipientResponse>( list.size() );
        for ( Recipient recipient : list ) {
            list1.add( recipientToRecipientResponse( recipient ) );
        }

        return list1;
    }
}
