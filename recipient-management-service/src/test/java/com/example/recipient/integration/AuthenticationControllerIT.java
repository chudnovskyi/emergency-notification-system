package com.example.recipient.integration;

import com.example.recipient.builder.AuthenticationJson;
import com.example.recipient.builder.AuthenticationJsonBuilder;
import com.example.recipient.builder.RecipientJson;
import com.example.recipient.config.ITBase;
import com.example.recipient.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.recipient.enums.Url.*;
import static com.example.recipient.integration.RecipientControllerIT.FIRST_RECIPIENT;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AuthenticationControllerIT extends ITBase {

    private final MockMvc mockMvc;
    private final MessageSourceService message;

    static final AuthenticationJson CLIENT = AuthenticationJsonBuilder.builder()
            .email("dummy")
            .password("password")
            .build();

    static final AuthenticationJson EXISTENT_CLIENT = AuthenticationJsonBuilder.builder()
            .email("authenitcated@gmail.com")
            .password("password")
            .build();

    @Test
    public void testRegisterSuccess() throws Exception {
        registerClientReturnTrue(CLIENT);
        registerClientEmailAlreadyExists(CLIENT);
    }

    @Test
    public void testRegisterFailure() throws Exception {
        registerClientEmailAlreadyExists(EXISTENT_CLIENT);
    }

    @Test
    public void testAuthenticationSuccessAndLogout() throws Exception {
        registerClientReturnTrue(CLIENT);
        String jwt = authenticationReturnJwt(CLIENT);
        registerRecipientSuccess(FIRST_RECIPIENT, jwt);
        logout(jwt);
        registerRecipientForbidden(FIRST_RECIPIENT, jwt);
    }

    @Test
    public void testAuthenticationFailure() throws Exception {
        String jwt = "dummy";
        registerRecipientBadJwt(FIRST_RECIPIENT, jwt);

        authenticationBadCredentials(CLIENT);
    }

    @Test
    public void clientIsolationTest() throws Exception {
        registerClientReturnTrue(CLIENT);

        String firstClientJwt = authenticationReturnJwt(CLIENT);
        String secondClientJwt = authenticationReturnJwt(EXISTENT_CLIENT);

        Long firstRecId = registerRecipientSuccess(FIRST_RECIPIENT, firstClientJwt);
        receiveRecipientSuccess(firstRecId, firstClientJwt);
        receiveRecipientNotFound(firstRecId, secondClientJwt);

        Long secondRecId = registerRecipientSuccess(FIRST_RECIPIENT, secondClientJwt);
        receiveRecipientNotFound(secondRecId, firstClientJwt);
        receiveRecipientSuccess(secondRecId, secondClientJwt);
    }

    private void registerClientEmailAlreadyExists(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(REGISTER.toString())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value(message.getProperty("client.email.already_exists"))
                );
    }

    private void registerClientReturnTrue(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(REGISTER.toString())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value("true")
                );
    }

    private String authenticationReturnJwt(AuthenticationJson authenticationJson) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(AUTHENTICATE.toString())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.jwt").isString()
                );

        return extractFieldFromResponse(resultActions, "jwt");
    }

    private void authenticationBadCredentials(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(AUTHENTICATE.toString())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    private Long registerRecipientSuccess(RecipientJson recipientJson, String jwt) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(RECIPIENTS.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
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

    private void registerRecipientForbidden(RecipientJson recipientJson, String jwt) throws Exception {
        mockMvc.perform(post(RECIPIENTS.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isForbidden()
                );
    }

    private void registerRecipientBadJwt(RecipientJson recipientJson, String jwt) throws Exception {
        mockMvc.perform(post(RECIPIENTS.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content(recipientJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isForbidden()
                );
    }

    private void receiveRecipientSuccess(Long id, String jwt) throws Exception {
        mockMvc.perform(get(RECIPIENTS.toString() + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpectAll(
                        status().isOk(),
                        content().string(not(emptyString()))
                );
    }

    private void receiveRecipientNotFound(Long id, String jwt) throws Exception {
        mockMvc.perform(get(RECIPIENTS.toString() + id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(message.getProperty("recipient.not_found", id))
                );
    }

    private void logout(String jwt) throws Exception {
        mockMvc.perform(get(LOGOUT.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpectAll(
                        status().isOk()
                );
    }
}
