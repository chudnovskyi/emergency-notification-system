package com.example.recipient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Url {
    RECIPIENTS("/api/v1/recipients/");

    private final String constant;

    @Override
    public String toString() {
        return constant;
    }
}
