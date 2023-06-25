package com.example.recipient.service;

import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Client;
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

    public RecipientResponse register(Client client, RecipientRequest request) {
        Optional<Recipient> existing = recipientRepository.findByEmailAndClient_Id(request.email(), client.getId());
        if (existing.isPresent()) {
            return update(client, existing.get().getId(), request);
        }

        try {
            return Optional.of(request)
                    .map(mapper::mapToEntity)
                    .map(client::addRecipient)
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
        return recipientRepository.findByIdAndClient_Id(recipientId, clientId)
                .map(mapper::mapToResponse)
                .orElseThrow(() -> new RecipientNotFoundException(
                        message.getProperty("recipient.not_found", recipientId)
                ));
    }

    public Boolean delete(Client client, Long recipientId) {
        return recipientRepository.findByIdAndClient_Id(recipientId, client.getId())
                .map(recipient -> {
                    recipientRepository.delete(recipient);
                    return recipient;
                })
                .isPresent();
    }

    public RecipientResponse update(Client client, Long recipientId, RecipientRequest request) {
        try {
            return recipientRepository.findByIdAndClient_Id(recipientId, client.getId())
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
