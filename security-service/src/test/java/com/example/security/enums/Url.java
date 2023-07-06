package com.example.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Url {
    REGISTER("/api/v1/auth/register"),
    AUTHENTICATE("/api/v1/auth/authenticate"),
    VALIDATE("/api/v1/auth/validate"),
    LOGOUT("/api/v1/auth/logout");

    private final String constant;

    @Override
    public String toString() {
        return constant;
    }
}
