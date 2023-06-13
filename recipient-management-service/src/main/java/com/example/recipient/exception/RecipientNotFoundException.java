package com.example.recipient.exception;

import jakarta.persistence.EntityNotFoundException;

public class RecipientNotFoundException extends EntityNotFoundException {

    public RecipientNotFoundException(String message) {
        super(message);
    }
}
