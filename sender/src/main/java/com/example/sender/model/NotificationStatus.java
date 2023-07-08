package com.example.sender.model;

public enum NotificationStatus implements EnumeratedEntityField {
    PENDING("P"),   // is waiting to be sent
    RESENDING("R"), // is queued for re-sending
    SENT("S"),      // has been successfully sent
    ERROR("E"),     // encountered an `retryAttempts` amount of errors during sending
    CORRUPT("C");   // impossible to sent (no such recipient, etc)

    private final String code;

    NotificationStatus(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
