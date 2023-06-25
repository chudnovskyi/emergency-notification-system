package com.example.recipient.exception.client;

import jakarta.persistence.EntityNotFoundException;

public class ClientNotFoundException extends EntityNotFoundException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
