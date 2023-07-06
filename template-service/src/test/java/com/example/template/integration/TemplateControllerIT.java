package com.example.template.integration;

import com.example.template.config.ITBase;
import com.example.template.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TemplateControllerIT extends ITBase {

    private final MockMvc mockMvc;
    private final MessageSourceService message;

    @Test
    public void test() throws Exception {
    }
}
