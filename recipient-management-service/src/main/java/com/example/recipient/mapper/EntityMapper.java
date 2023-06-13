package com.example.recipient.mapper;

import org.mapstruct.MappingTarget;

public interface EntityMapper<Entity, RequestDTO, ResponseDTO> {

    Entity mapToEntity(RequestDTO request);

    ResponseDTO mapToResponse(Entity entity);

    Entity update(RequestDTO request, @MappingTarget Entity entity);
}
