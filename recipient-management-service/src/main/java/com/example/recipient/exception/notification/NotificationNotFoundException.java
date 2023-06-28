package com.example.recipient.exception.notification;

import jakarta.persistence.EntityNotFoundException;

public class NotificationNotFoundException extends EntityNotFoundException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
