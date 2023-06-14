package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Recipient;
import com.example.recipient.exception.RecipientNotFoundException;
import com.example.recipient.exception.RecipientRegistrationException;
import com.example.recipient.mapper.RecipientMapper;
import com.example.recipient.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final RecipientMapper mapper;
    private final MessageSourceService message;

    public Long register(RecipientRequest request) {
        if (request.email() != null && recipientRepository.findByEmail(request.email()).isPresent()) {
            Recipient recipient = recipientRepository.findByEmail(request.email()).orElseThrow();
            Recipient update = mapper.update(request, recipient);
            recipientRepository.saveAndFlush(update);
            return recipient.getId();
        }

        try {
            return Optional.of(request)
                    .map(mapper::mapToEntity)
                    .map(recipientRepository::save)
                    .map(Recipient::getId)
                    .orElseThrow();
        } catch (DataIntegrityViolationException e) {
            throw new RecipientRegistrationException(e.getMessage());
        }
    }

    public RecipientResponse receive(Long id) {
        return recipientRepository.findById(id)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> {
                    throw new RecipientNotFoundException(message.getProperty("recipient.not_found", id));
                });
    }

    public Boolean delete(Long id) {
        return recipientRepository.findById(id)
                .map(recipient -> {
                    recipientRepository.delete(recipient);
                    return recipient;
                })
                .isPresent();
    }

    public RecipientResponse update(Long id, RecipientRequest request) {
        return recipientRepository.findById(id)
                .map(recipient -> mapper.update(request, recipient))
                .map(recipientRepository::saveAndFlush)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> {
                    throw new RecipientNotFoundException(message.getProperty("recipient.not_found", id));
                });
    }
}
