package com.example.recipient.builder;

public record RecipientJson(
        String email,
        String phoneNumber,
        String telegramId
) implements Product {

    private static final String TEMPLATE = """
            {
                "name": "dummy",
                "email": %s,
                "phoneNumber": %s,
                "telegramId": %s,
                "geolocation": {
                    "latitude": 90.0000,
                    "longitude": -90.0000
                }
            }
            """;

    @Override
    public String toJson() {
        String emailVal = format(email);
        String phoneVal = format(phoneNumber);
        String telegramVal = format(telegramId);
        return String.format(TEMPLATE, emailVal, phoneVal, telegramVal);
    }
}
