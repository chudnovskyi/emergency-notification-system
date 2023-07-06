package com.example.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Url {
    FILES("/api/v1/files/");

    private final String constant;

    @Override
    public String toString() {
        return constant;
    }
}
