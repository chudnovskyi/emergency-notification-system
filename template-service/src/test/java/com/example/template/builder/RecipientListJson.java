package com.example.template.builder;

import org.springframework.util.StringUtils;

import java.util.List;

public record RecipientListJson(
        List<Integer> recipientIds
) {

    private static final String TEMPLATE = """
            {
                 "recipientIds": [
                     %s
                 ]
             }
            """;

    public String toJson() {
        return TEMPLATE.formatted(StringUtils.collectionToDelimitedString(recipientIds, ","));
    }
}
