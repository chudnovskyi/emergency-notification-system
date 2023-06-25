package com.example.recipient.exception.client;

import org.springframework.security.authentication.BadCredentialsException;

public class ClientBadCredentialsException extends BadCredentialsException {

    public ClientBadCredentialsException(String message) {
        super(message);
    }
}
