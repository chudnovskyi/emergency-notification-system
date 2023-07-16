package com.example.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Url {
    CREATE("/api/v1/templates/"),
    GET("/api/v1/templates/%s"),
    DELETE("/api/v1/templates/%s"),
    ADD_REC("/api/v1/templates/%s/recipients"),
    DEL_REC("/api/v1/templates/%s/recipients"),
    HISTORY_CREATE("/api/v1/templates/history/%s"),
    HISTORY_GET("/api/v1/templates/history/%s");

    private final String url;
}
