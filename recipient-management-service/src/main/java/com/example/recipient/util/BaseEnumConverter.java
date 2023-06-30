package com.example.recipient.util;

import com.example.recipient.model.EnumeratedEntityField;
import jakarta.persistence.AttributeConverter;

import java.util.stream.Stream;

public class BaseEnumConverter<T extends Enum<T> & EnumeratedEntityField> implements AttributeConverter<T, String> {

    private final Class<T> enumClass;

    public BaseEnumConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public T convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(enumClass.getEnumConstants())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
