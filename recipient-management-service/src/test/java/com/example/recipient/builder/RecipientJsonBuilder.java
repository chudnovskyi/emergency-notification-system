package com.example.recipient.builder;

public class RecipientJsonBuilder extends Builder<RecipientJson> {

    private String email;
    private String phoneNumber;
    private String telegramId;

    public static RecipientJsonBuilder builder() {
        return new RecipientJsonBuilder();
    }

    public RecipientJsonBuilder email(String email) {
        this.email = email;
        return this;
    }

    public RecipientJsonBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RecipientJsonBuilder telegramId(String telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    @Override
    public RecipientJson build() {
        return new RecipientJson(email, phoneNumber, telegramId);
    }
}

