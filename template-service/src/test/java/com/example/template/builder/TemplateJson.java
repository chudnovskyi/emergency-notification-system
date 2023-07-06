package com.example.template.builder;

public record TemplateJson(
        String title,
        String content
) implements Product {

    private static final String TEMPLATE = """
            {
                "title": %s,
                "content": %s
            }
            """;

    @Override
    public String toJson() {
        String titleVal = format(title);
        String contentVal = format(content);
        return TEMPLATE.formatted(titleVal, contentVal);
    }
}
