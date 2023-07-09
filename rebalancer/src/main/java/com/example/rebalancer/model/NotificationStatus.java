package com.example.rebalancer.model;

public enum NotificationStatus {
    NEW("N"),       // has just been created
    PENDING("P"),   // is currently in the sending process
    SENT("S"),      // has been successfully sent
    RESENDING("R"), // is queued for re-sending
    ERROR("E"),     // encountered a certain number of errors during sending
    CORRUPT("C");   // is impossible to send (e.g., due to invalid recipient)

    NotificationStatus(String code) {
    }
}
