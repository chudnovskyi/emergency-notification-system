package com.example.recipient.integration;

import com.example.recipient.builder.RecipientJson;
import com.example.recipient.builder.RecipientJsonBuilder;
import com.example.recipient.config.ITBase;
import com.example.recipient.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.recipient.enums.Url.RECIPIENTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class RecipientControllerIT extends ITBase {

    private final MockMvc mockMvc;
    private final MessageSourceService message;

    static final RecipientJson FIRST_RECIPIENT = RecipientJsonBuilder.builder()
            .email("viacheslav@gmail.com")
            .phoneNumber("+380973078623")
            .telegramId("lwantPizza")
            .build();

    static final RecipientJson SECOND_RECIPIENT = RecipientJsonBuilder.builder()
            .email("dummy_1@gmail.com")
            .phoneNumber("+123456789")
            .build();

    static final RecipientJson REPEATED_PHONE_RECIPIENT = RecipientJsonBuilder.builder()
            .email("dummy_2@gmail.com")
            .phoneNumber("+123456789")
            .telegramId("dummy_tg")
            .build();

    static final RecipientJson REPEATED_EMAIL_RECIPIENT = RecipientJsonBuilder.builder()
            .email("viacheslav@gmail.com")
            .telegramId("new-tg")
            .build();

    @Test
    public void receiveNotFoundTest() throws Exception {
        receiveNotFound(-1L);
    }

    @Test
    public void registerTest() throws Exception {
        Long firstRecId = registerSuccess(FIRST_RECIPIENT);
        Long overwriteRecId = registerSuccess(REPEATED_EMAIL_RECIPIENT);
        assertThat(firstRecId).isEqualTo(overwriteRecId);

        String phoneNumber = receiveSuccess(overwriteRecId, "phoneNumber");
        assertThat(phoneNumber.equals("null") ? null : phoneNumber).isEqualTo(REPEATED_EMAIL_RECIPIENT.phoneNumber());
        String telegramId = receiveSuccess(overwriteRecId, "telegramId");
        assertThat(telegramId.equals("null") ? null : telegramId).isEqualTo(REPEATED_EMAIL_RECIPIENT.telegramId());

        registerSuccess(SECOND_RECIPIENT);
        registerFailure(REPEATED_PHONE_RECIPIENT);
    }

    @Test
    public void deleteTest() throws Exception {
        deleteFailure(1L);
        Long recipientId = registerSuccess(FIRST_RECIPIENT);
        deleteSuccess(recipientId);
        receiveNotFound(recipientId);
        deleteFailure(recipientId);
        Long newRecipientId = registerSuccess(FIRST_RECIPIENT);

        assertThat(recipientId).isNotEqualTo(newRecipientId);
    }

    @Test
    public void updateTest() throws Exception {
        updateNotFound(-1L, FIRST_RECIPIENT);
        Long recipientId = registerSuccess(FIRST_RECIPIENT);
        updateSuccess(recipientId, SECOND_RECIPIENT);

        String updatedEmail = receiveSuccess(recipientId, "email");
        assertThat(updatedEmail).isNotEqualTo(FIRST_RECIPIENT.email());
        assertThat(updatedEmail).isEqualTo(SECOND_RECIPIENT.email());
    }

    private Long registerSuccess(RecipientJson recipientJson) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(RECIPIENTS.toString()).with(user(userDetails))
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.email").value(recipientJson.email()),
                        jsonPath("$.phoneNumber").value(recipientJson.phoneNumber()),
                        jsonPath("$.telegramId").value(recipientJson.telegramId())
                );

        return Long.valueOf(extractFieldFromResponse(resultActions, "id"));
    }

    private void registerFailure(RecipientJson recipientJson) throws Exception {
        mockMvc.perform(post(RECIPIENTS.toString()).with(user(userDetails))
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value(containsString("ERROR: duplicate key value violates unique constraint"))
                );
    }

    private String receiveSuccess(Long id, String fieldToExtract) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(RECIPIENTS.toString() + id).with(user(userDetails)))
                .andExpectAll(
                        status().isOk(),
                        content().string(not(emptyString()))
                );

        return extractFieldFromResponse(resultActions, fieldToExtract);
    }

    private void receiveNotFound(Long id) throws Exception {
        mockMvc.perform(get(RECIPIENTS.toString() + id).with(user(userDetails)))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(message.getProperty("recipient.not_found", id))
                );
    }

    private void deleteSuccess(Long id) throws Exception {
        deleteAndExpect(id, "true");
    }

    private void deleteFailure(Long id) throws Exception {
        deleteAndExpect(id, "false");
    }

    private void deleteAndExpect(Long id, String expected) throws Exception {
        mockMvc.perform(delete(RECIPIENTS.toString() + id).with(user(userDetails)))
                .andExpectAll(
                        status().isOk(),
                        content().string(expected)
                );
    }

    private void updateSuccess(Long id, RecipientJson recipientJson) throws Exception {
        mockMvc.perform(patch(RECIPIENTS.toString() + id).with(user(userDetails))
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().string(not(emptyString()))
                );
    }

    private void updateFailure(Long id, RecipientJson recipientJson) throws Exception {
        mockMvc.perform(patch(RECIPIENTS.toString() + id).with(user(userDetails))
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    private void updateNotFound(Long id, RecipientJson recipientJson) throws Exception {
        mockMvc.perform(patch(RECIPIENTS.toString() + id).with(user(userDetails))
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(message.getProperty("recipient.not_found", id))
                );
    }
}
