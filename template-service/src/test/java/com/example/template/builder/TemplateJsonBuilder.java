package com.example.template.builder;

public class TemplateJsonBuilder extends Builder<TemplateJson> {

    private String title;
    private String content;

    public static TemplateJsonBuilder builder() {
        return new TemplateJsonBuilder();
    }

    public TemplateJsonBuilder title(String email) {
        this.title = email;
        return this;
    }

    public TemplateJsonBuilder content(String password) {
        this.content = password;
        return this;
    }

    @Override
    public TemplateJson build() {
        return new TemplateJson(title, content);
    }
}
