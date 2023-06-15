package com.example.recipient.exception;

import jakarta.persistence.EntityNotFoundException;

public class ClientNotFoundException extends EntityNotFoundException {

    public ClientNotFoundException(String message) {
        super(message);
    }
}
