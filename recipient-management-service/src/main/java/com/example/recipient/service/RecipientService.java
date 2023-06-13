package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.entity.Recipient;
import com.example.recipient.mapper.RecipientMapper;
import com.example.recipient.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final RecipientMapper recipientMapper;

    public Long register(RecipientRequest request) {
        return Optional.of(request)
                .map(recipientMapper::mapToEntity)
                .map(recipientRepository::save)
                .map(Recipient::getId)
                .orElseThrow();
    }
}
