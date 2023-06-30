package com.example.sender.model;

public enum NotificationType implements EnumeratedEntityField {
    EMAIL("EML"),
    PHONE("PHN"),
    TELEGRAM("TGM");

    private final String code;

    NotificationType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
