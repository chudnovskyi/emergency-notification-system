package com.example.recipient.builder;

public record RecipientJson(
        String email,
        String phoneNumber,
        String telegramId
) {
    private static final String RECIPIENT_PATTERN_TEMPLATE = """
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

    public String toJson() {
        String emailVal = formatVal(email);
        String phoneVal = formatVal(phoneNumber);
        String telegramVal = formatVal(telegramId);
        return String.format(RECIPIENT_PATTERN_TEMPLATE, emailVal, phoneVal, telegramVal);
    }

    private String formatVal(String value) {
        if (value != null) {
            return "\"" + value + "\"";
        } else {
            return "null";
        }
    }
}
