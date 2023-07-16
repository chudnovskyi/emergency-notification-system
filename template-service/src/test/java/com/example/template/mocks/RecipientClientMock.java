package com.example.template.mocks;

import com.example.template.client.RecipientClient;
import com.example.template.dto.response.RecipientResponse;
import feign.FeignException;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class RecipientClientMock {

    public static void setupMockRecipientClient(RecipientClient recipientClient) {
        when(
                recipientClient.receiveRecipientResponseListByTemplateId(any(), any())
        ).thenReturn(
                ResponseEntity.ofNullable(Collections.emptyList())
        );

        createWhenReceiveRecipientById(recipientClient, 1L);
        createWhenReceiveRecipientById(recipientClient, 2L);

        when(
                recipientClient.receiveRecipientById(any(), eq(3L))
        ).thenThrow(FeignException.NotFound.class);
    }

    private static void createWhenReceiveRecipientById(RecipientClient recipientClient, Long id) {
        when(
                recipientClient.receiveRecipientById(any(), eq(id))
        ).thenReturn(
                ResponseEntity.ofNullable(RecipientResponse.builder().id(id).build())
        );
    }
}
