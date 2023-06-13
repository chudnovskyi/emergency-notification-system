package com.example.recipient.mapper;

public interface EntityMapper<Entity, RequestDTO, ResponseDTO> {

    Entity mapToEntity(RequestDTO request);

    ResponseDTO mapToResponse(Entity entity);
}
