package com.example.shortener.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {

    T getId();
}
