package com.example.shortener.service;

import com.example.shortener.entity.Response;
import com.example.shortener.mapper.ResponseMapper;
import com.example.shortener.model.request.NotificationOptionRequest;
import com.example.shortener.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final ResponseMapper mapper;

    public Long createResponse(NotificationOptionRequest request) {
        return Optional.of(request)
                .map(mapper::mapToResponse)
                .map(responseRepository::saveAndFlush)
                .map(Response::getId)
                .orElseThrow();
    }
}
