package com.example.recipient.exception;

public class InvalidFileFormatException extends RuntimeException {

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
