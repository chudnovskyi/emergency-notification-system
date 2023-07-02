package com.example.recipient.mapper;

import com.example.recipient.dto.request.GeolocationRequest;
import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.GeolocationResponse;
import com.example.recipient.dto.response.GeolocationResponse.GeolocationResponseBuilder;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.dto.response.RecipientResponse.RecipientResponseBuilder;
import com.example.recipient.entity.Geolocation;
import com.example.recipient.entity.Recipient;
import com.example.recipient.entity.Recipient.RecipientBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-02T02:54:40+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class RecipientMapperImpl implements RecipientMapper {

    @Override
    public Recipient mapToEntity(RecipientRequest request) {
        if ( request == null ) {
            return null;
        }

        RecipientBuilder recipient = Recipient.builder();

        recipient.name( request.name() );
        recipient.geolocation( geolocationRequestToGeolocation( request.geolocation() ) );
        recipient.email( request.email() );
        recipient.telegramId( request.telegramId() );
        recipient.phoneNumber( request.phoneNumber() );

        return recipient.build();
    }

    @Override
    public RecipientResponse mapToResponse(Recipient entity) {
        if ( entity == null ) {
            return null;
        }

        RecipientResponseBuilder recipientResponse = RecipientResponse.builder();

        recipientResponse.id( entity.getId() );
        recipientResponse.name( entity.getName() );
        recipientResponse.email( entity.getEmail() );
        recipientResponse.phoneNumber( entity.getPhoneNumber() );
        recipientResponse.telegramId( entity.getTelegramId() );
        recipientResponse.geolocation( geolocationToGeolocationResponse( entity.getGeolocation() ) );

        return recipientResponse.build();
    }

    @Override
    public Recipient update(RecipientRequest request, Recipient entity) {
        if ( request == null ) {
            return null;
        }

        entity.setName( request.name() );
        if ( request.geolocation() != null ) {
            if ( entity.getGeolocation() == null ) {
                entity.setGeolocation( new Geolocation() );
            }
            geolocationRequestToGeolocation1( request.geolocation(), entity.getGeolocation() );
        }
        else {
            entity.setGeolocation( null );
        }
        entity.setEmail( request.email() );
        entity.setTelegramId( request.telegramId() );
        entity.setPhoneNumber( request.phoneNumber() );

        return entity;
    }

    protected Geolocation geolocationRequestToGeolocation(GeolocationRequest geolocationRequest) {
        if ( geolocationRequest == null ) {
            return null;
        }

        Geolocation geolocation = new Geolocation();

        geolocation.setLatitude( geolocationRequest.latitude() );
        geolocation.setLongitude( geolocationRequest.longitude() );

        return geolocation;
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

    protected void geolocationRequestToGeolocation1(GeolocationRequest geolocationRequest, Geolocation mappingTarget) {
        if ( geolocationRequest == null ) {
            return;
        }

        mappingTarget.setLatitude( geolocationRequest.latitude() );
        mappingTarget.setLongitude( geolocationRequest.longitude() );
    }
}
