package com.example.recipient.mapper;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Recipient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipientMapper extends EntityMapper<Recipient, RecipientRequest, RecipientResponse> {

}
