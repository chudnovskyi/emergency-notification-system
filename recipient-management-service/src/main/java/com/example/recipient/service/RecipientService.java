package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Recipient;
import com.example.recipient.exception.recipient.RecipientNotFoundException;
import com.example.recipient.exception.recipient.RecipientRegistrationException;
import com.example.recipient.mapper.RecipientMapper;
import com.example.recipient.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipientService {

    private final RecipientRepository recipientRepository;
    private final RecipientMapper mapper;
    private final MessageSourceService message;

    public RecipientResponse register(Long clientId, RecipientRequest request) {
        Optional<Recipient> existing = recipientRepository.findByEmailAndClientId(request.email(), clientId);
        if (existing.isPresent()) {
            return update(clientId, existing.get().getId(), request);
        }

        try {
            return Optional.of(request)
                    .map(mapper::mapToEntity)
                    .map(recipient -> recipient.addClient(clientId))
                    .map(recipientRepository::save)
                    .map(mapper::mapToResponse)
                    .orElseThrow(() -> new RecipientRegistrationException(
                            message.getProperty("recipient.registration", request.email())
                    ));
        } catch (DataIntegrityViolationException e) {
            throw new RecipientRegistrationException(e.getMessage());
        }
    }

    public RecipientResponse receive(Long clientId, Long recipientId) {
        return recipientRepository.findByIdAndClientId(recipientId, clientId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new RecipientNotFoundException(
                        message.getProperty("recipient.not_found", recipientId)
                ));
    }

    public Boolean delete(Long clientId, Long recipientId) {
        return recipientRepository.findByIdAndClientId(recipientId, clientId)
                .map(recipient -> {
                    recipientRepository.delete(recipient);
                    return recipient;
                })
                .isPresent();
    }

    public RecipientResponse update(Long clientId, Long recipientId, RecipientRequest request) {
        try {
            return recipientRepository.findByIdAndClientId(recipientId, clientId)
                    .map(recipient -> mapper.update(request, recipient))
                    .map(recipientRepository::saveAndFlush)
                    .map(mapper::mapToResponse)
                    .orElseThrow(() -> new RecipientNotFoundException(
                            message.getProperty("recipient.not_found", recipientId)
                    ));
        } catch (DataIntegrityViolationException e) {
            throw new RecipientRegistrationException(e.getMessage());
        }
    }
}
