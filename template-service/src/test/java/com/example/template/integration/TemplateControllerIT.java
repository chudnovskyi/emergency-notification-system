package com.example.template.integration;

import com.example.template.ITBase;
import com.example.template.builder.RecipientListJson;
import com.example.template.builder.TemplateJson;
import com.example.template.builder.TemplateJsonBuilder;
import com.example.template.client.RecipientClient;
import com.example.template.mocks.RecipientClientMock;
import com.example.template.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.example.template.enums.Url.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class TemplateControllerIT extends ITBase {

    private final MockMvc mockMvc;
    private final MessageSourceService message;

    static final Long CLIENT_ID = 1L;

    static final TemplateJson TEMPLATE = TemplateJsonBuilder.builder()
            .content("content")
            .title("title")
            .build();

    static final RecipientListJson RECIPIENT_LIST_JSON = new RecipientListJson(List.of(1, 2, 3, 1));

    @MockBean
    private final RecipientClient recipientClient;

    @BeforeEach
    void setUp() {
        RecipientClientMock.setupMockRecipientClient(recipientClient);
    }

    @Test
    public void createTemplateTest() throws Exception {
        String id = createTemplateCreated(TEMPLATE);
        assertThat(id).isNotEmpty();

        createTemplateConflict(TEMPLATE);
    }

    @Test
    public void getTemplateTest() throws Exception {
        getTemplateNotFound(1L);

        String id = createTemplateCreated(TEMPLATE);

        getTemplateOk(Long.valueOf(id), TEMPLATE);
    }

    @Test
    public void deleteTemplateTest() throws Exception {
        deleteTemplateAndExpectResult(1L, false);

        Long id = Long.valueOf(createTemplateCreated(TEMPLATE));
        getTemplateOk(id, TEMPLATE);

        deleteTemplateAndExpectResult(id, true);
        getTemplateNotFound(id);
    }

    @Test
    public void addRecipientsTest() throws Exception {
        addRecipientsTemplateNotFound(1L, RECIPIENT_LIST_JSON);

        Long id = Long.valueOf(createTemplateCreated(TEMPLATE));
        getTemplateOk(id, TEMPLATE);

        addRecipientsTemplateCreated(id, RECIPIENT_LIST_JSON, TEMPLATE);
        getTemplateOk(id, TEMPLATE);
    }

    @Test
    public void deleteRecipientsTest() throws Exception {
        deleteRecipientsTemplateNotFound(1L, RECIPIENT_LIST_JSON);

        Long id = Long.valueOf(createTemplateCreated(TEMPLATE));
        getTemplateOk(id, TEMPLATE);

        addRecipientsTemplateCreated(id, new RecipientListJson(List.of(1)), TEMPLATE);
        deleteRecipientsTemplateOk(id, RECIPIENT_LIST_JSON, TEMPLATE);
        getTemplateOk(id, TEMPLATE);
    }

    private String createTemplateCreated(TemplateJson template) throws Exception {
        ResultActions result = mockMvc.perform(post(CREATE.getUrl())
                        .header("clientId", CLIENT_ID)
                        .content(template.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
        return extractJsonValueByKey(result, "id");
    }

    private void createTemplateConflict(TemplateJson template) throws Exception {
        mockMvc.perform(post(CREATE.getUrl())
                        .header("clientId", CLIENT_ID)
                        .content(template.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.message").value(
                                message.getProperty("template.title_already_exists", template.title(), CLIENT_ID)
                        )
                );
    }

    private void getTemplateNotFound(Long templateId) throws Exception {
        mockMvc.perform(get(GET.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                message.getProperty("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void getTemplateOk(Long templateId, TemplateJson template) throws Exception {
        mockMvc.perform(get(GET.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isEmpty()
                );
    }

    private void deleteTemplateAndExpectResult(Long templateId, Boolean result) throws Exception {
        mockMvc.perform(delete(DELETE.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value(result)
                );
    }

    private void addRecipientsTemplateNotFound(Long templateId, RecipientListJson recipients) throws Exception {
        mockMvc.perform(post(ADD_REC.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                message.getProperty("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void addRecipientsTemplateCreated(Long templateId, RecipientListJson recipients, TemplateJson template) throws Exception {
        mockMvc.perform(post(ADD_REC.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }

    private void deleteRecipientsTemplateNotFound(Long templateId, RecipientListJson recipients) throws Exception {
        mockMvc.perform(delete(DEL_REC.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(
                                message.getProperty("template.not_found", templateId, CLIENT_ID)
                        )
                );
    }

    private void deleteRecipientsTemplateOk(Long templateId, RecipientListJson recipients, TemplateJson template) throws Exception {
        mockMvc.perform(delete(DEL_REC.getUrl().formatted(templateId))
                        .header("clientId", CLIENT_ID)
                        .content(recipients.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").exists(),
                        jsonPath("$.title").value(template.title()),
                        jsonPath("$.content").value(template.content()),
                        jsonPath("$.imageUrl").isEmpty(),
                        jsonPath("$.recipientIds").isArray()
                );
    }
}
