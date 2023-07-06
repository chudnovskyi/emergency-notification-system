package com.example.security.builder;

public interface Product {

    String toJson();

    default String format(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
