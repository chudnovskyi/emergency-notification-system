package com.example.recipient.builder;

public record AuthenticationJson(
        String email,
        String password
) implements Product {

    private static final String TEMPLATE = """
            {
                "email": %s,
                "password": %s
            }
            """;

    @Override
    public String toJson() {
        String emailVal = format(email);
        String passwordVal = format(password);
        return TEMPLATE.formatted(emailVal, passwordVal);
    }
}
