package com.example.recipient.builder;

public interface Product {

    String toJson();

    default String format(String string) {
        return string == null ? "null" : "\"" + string + "\"";
    }
}
