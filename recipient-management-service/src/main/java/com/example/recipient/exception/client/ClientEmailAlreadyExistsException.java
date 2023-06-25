package com.example.recipient.exception.client;

import jakarta.persistence.EntityExistsException;

public class ClientEmailAlreadyExistsException extends EntityExistsException {

    public ClientEmailAlreadyExistsException(String message) {
        super(message);
    }
}
