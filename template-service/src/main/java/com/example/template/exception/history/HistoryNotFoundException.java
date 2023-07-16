package com.example.template.exception.history;

import jakarta.persistence.EntityNotFoundException;

public class HistoryNotFoundException extends EntityNotFoundException {

    public HistoryNotFoundException(String message) {
        super(message);
    }
}
