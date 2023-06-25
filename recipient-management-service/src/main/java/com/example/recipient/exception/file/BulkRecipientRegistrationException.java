package com.example.recipient.exception.file;

import lombok.Getter;

import java.util.Map;

@Getter
public class BulkRecipientRegistrationException extends RuntimeException {

    public Map<String, String> errors;

    public BulkRecipientRegistrationException(Map<String, String> errors) {
        this.errors = errors;
    }
}
