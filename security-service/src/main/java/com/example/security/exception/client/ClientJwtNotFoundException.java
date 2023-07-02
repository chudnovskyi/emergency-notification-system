package com.example.security.exception.client;

import io.jsonwebtoken.JwtException;

public class ClientJwtNotFoundException extends JwtException {

    public ClientJwtNotFoundException(String message) {
        super(message);
    }
}
