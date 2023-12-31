package com.example.shortener.mapper;

import com.example.shortener.entity.Response;
import com.example.shortener.dto.request.NotificationOptionsRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {

    Response mapToResponse(NotificationOptionsRequest request);
}
