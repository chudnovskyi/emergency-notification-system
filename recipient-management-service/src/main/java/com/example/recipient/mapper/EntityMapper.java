package com.example.recipient.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface EntityMapper<Entity, RequestDTO, ResponseDTO> {

    @Mapping(target = "id", ignore = true)
    Entity mapToEntity(RequestDTO request);

    ResponseDTO mapToResponse(Entity entity);

    @Mapping(target = "id", ignore = true)
    Entity update(RequestDTO request, @MappingTarget Entity entity);
}
