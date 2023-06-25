package com.example.recipient.exception.template;

import jakarta.persistence.EntityExistsException;

public class TemplateTitleAlreadyExistsException extends EntityExistsException {

    public TemplateTitleAlreadyExistsException(String message) {
        super(message);
    }
}
