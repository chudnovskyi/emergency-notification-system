package com.example.recipient.builder;

public class AuthenticationJsonBuilder extends Builder<AuthenticationJson> {

    private String email;
    private String password;

    public static AuthenticationJsonBuilder builder() {
        return new AuthenticationJsonBuilder();
    }

    public AuthenticationJsonBuilder email(String email) {
        this.email = email;
        return this;
    }

    public AuthenticationJsonBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Override
    public AuthenticationJson build() {
        return new AuthenticationJson(email, password);
    }
}
