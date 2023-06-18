package com.example.recipient.exception;

public class ClientEmailAlreadyExists extends RuntimeException {

    public ClientEmailAlreadyExists(String message) {
        super(message);
    }
}
