package com.example.recipient.exception.template;

import jakarta.persistence.EntityNotFoundException;

public class TemplateRecipientsNotFound extends EntityNotFoundException {

    public TemplateRecipientsNotFound(String message) {
        super(message);
    }
}
