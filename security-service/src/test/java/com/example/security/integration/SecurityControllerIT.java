package com.example.security.integration;

import com.example.security.ITBase;
import com.example.security.builder.AuthenticationJson;
import com.example.security.builder.AuthenticationJsonBuilder;
import com.example.security.service.MessageSourceService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.security.enums.Url.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
public class SecurityControllerIT extends ITBase {

    private final MockMvc mockMvc;
    private final MessageSourceService message;

    static final AuthenticationJson CLIENT = AuthenticationJsonBuilder.builder()
            .email("dummy@gmail.com")
            .password("password")
            .build();

    static final AuthenticationJson EXISTENT_CLIENT = AuthenticationJsonBuilder.builder()
            .email("authenitcated@gmail.com")
            .password("password")
            .build();

    @Test
    public void testRegistration() throws Exception {
        registerClientReturnTrue(CLIENT);
        registerClientEmailAlreadyExists(CLIENT);
        registerClientEmailAlreadyExists(EXISTENT_CLIENT);
    }

    @Test
    public void testAuthenticationSuccess() throws Exception {
        registerClientReturnTrue(CLIENT);
        String jwt = authenticationReturnJwt(CLIENT);

        assertThat(jwt).isNotNull().isNotBlank();
    }

    @Test
    public void testAuthenticationFailure() throws Exception {
        authenticationNotFound(CLIENT);
        registerClientReturnTrue(CLIENT);

        AuthenticationJson invalidCredentialClient = AuthenticationJsonBuilder.builder()
                .email(CLIENT.email())
                .password(CLIENT.password() + "!")
                .build();
        authenticationBadCredentials(invalidCredentialClient);

        AuthenticationJson invalidValuesJson = AuthenticationJsonBuilder.builder()
                .email("@")
                .build();
        registerInvalidRequest(invalidValuesJson);
    }

    @Test
    public void testValidationSuccess() throws Exception {
        registerClientReturnTrue(CLIENT);
        String jwt = authenticationReturnJwt(CLIENT);
        validateSuccess(jwt);
    }

    @Test
    public void testValidationFailure() throws Exception {
        String dummyJwt = "dummy";
        validateForbidden(dummyJwt);
    }

    @Test
    public void testLogout() throws Exception {
        registerClientReturnTrue(CLIENT);
        String jwt = authenticationReturnJwt(CLIENT);
        validateSuccess(jwt);
        logout(jwt);
        validateInvalidJwt(jwt);
    }

    private void registerClientReturnTrue(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(REGISTER.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").value("true")
                );
    }

    private void registerInvalidRequest(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(REGISTER.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string(not(emptyString()))
                );
    }

    private void registerClientEmailAlreadyExists(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(REGISTER.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.message").value(message.getProperty("client.email.already_exists"))
                );
    }

    private String authenticationReturnJwt(AuthenticationJson authenticationJson) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(AUTHENTICATE.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.jwt").isString()
                );

        return extractJsonValueByKey(resultActions, "jwt");
    }

    private void authenticationBadCredentials(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(AUTHENTICATE.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message").value(message.getProperty("client.bad_cred"))
                );
    }

    private void authenticationNotFound(AuthenticationJson authenticationJson) throws Exception {
        mockMvc.perform(post(AUTHENTICATE.getUrl())
                        .content(authenticationJson.toJson())
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message").value(message.getProperty("client.not_found", authenticationJson.email()))
                );
    }

    private void validateSuccess(String jwt) throws Exception {
        mockMvc.perform(get(VALIDATE.getUrl())
                        .header("Authorization", "Bearer " + jwt))
                .andExpectAll(
                        status().isOk(),
                        content().string(not(emptyString()))
                );
    }

    private void validateForbidden(String jwt) throws Exception {
        mockMvc.perform(get(VALIDATE.getUrl())
                        .header("Authorization", "Bearer " + jwt))
                .andExpectAll(
                        status().isForbidden()
                );
    }

    private void validateInvalidJwt(String jwt) throws Exception {
        mockMvc.perform(get(VALIDATE.getUrl())
                        .header("Authorization", "Bearer " + jwt))
                .andExpectAll(
                        status().isForbidden(),
                        jsonPath("$.message").value(message.getProperty("jwt.invalid"))
                );
    }

    private void logout(String jwt) throws Exception {
        mockMvc.perform(get(LOGOUT.getUrl())
                        .header(AUTHORIZATION, "Bearer " + jwt))
                .andExpectAll(
                        status().isOk()
                );
    }
}
