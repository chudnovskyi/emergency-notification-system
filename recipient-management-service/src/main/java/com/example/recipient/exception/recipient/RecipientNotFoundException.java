package com.example.recipient.exception.recipient;

import jakarta.persistence.EntityNotFoundException;

public class RecipientNotFoundException extends EntityNotFoundException {

    public RecipientNotFoundException(String message) {
        super(message);
    }
}
