package com.example.recipient.exception.template;

import jakarta.persistence.EntityNotFoundException;

public class TemplateNotFoundException extends EntityNotFoundException {

    public TemplateNotFoundException(String message) {
        super(message);
    }
}
