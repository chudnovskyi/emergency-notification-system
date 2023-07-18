package com.example.shortener.mapper;

import com.example.shortener.entity.Response;
import com.example.shortener.model.request.NotificationOptionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {

    Response mapToResponse(NotificationOptionRequest request);
}
