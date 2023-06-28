package com.example.sender.model;

public enum NotificationStatus {
    PENDING("P"),  // is waiting to be sent
    SENT("S"),     // has been successfully sent
    ERROR("E"),    // encountered an `retryAttempts` amount of errors during sending
    RESEND("R"),   // is queued for re-sending
    CORRUPTED("C"); // impossible to sent (no such recipient, etc)

    private final String code;

    NotificationStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
