package com.example.recipient.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BulkRecipientRegistrationException extends IllegalArgumentException {

    public Map<String, String> errors;

    public BulkRecipientRegistrationException(Map<String, String> errors) {
        this.errors = errors;
    }
}
