package com.example.template.integration;

import com.example.template.ITBase;
import com.example.template.builder.RecipientListJson;
import com.example.template.client.RecipientClient;
import com.example.template.dto.kafka.Operation;
import com.example.template.dto.kafka.TemplateRecipientKafka;
import com.example.template.listener.KafkaListeners;
import com.example.template.mocks.RecipientClientMock;
import com.example.template.repository.RecipientIdRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.template.dto.kafka.Operation.PERSISTS;
import static com.example.template.dto.kafka.Operation.REMOVE;
import static com.example.template.enums.Url.ADD_REC;
import static com.example.template.enums.Url.CREATE;
import static com.example.template.integration.TemplateControllerIT.CLIENT_ID;
import static com.example.template.integration.TemplateControllerIT.TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RequiredArgsConstructor
public class KafkaListenerIT extends ITBase {

    @Value("${spring.kafka.topics.template-update}")
    private String consumerTopic;

    private final KafkaTemplate<String, TemplateRecipientKafka> kafkaTemplate;
    private final RecipientIdRepository recipientIdRepository;
    private final KafkaListeners kafkaListeners;
    private final MockMvc mockMvc;

    @MockBean
    private final RecipientClient recipientClient;

    @BeforeEach
    void setUp() {
        RecipientClientMock.setupMockRecipientClient(recipientClient);
    }

    @Test
    public void consumerTest() throws Exception {
        Long templateId = createTemplateCreated();
        addRecipientsTemplateCreated(templateId);
        Thread.sleep(500L);

        testKafkaListener(PERSISTS, templateId, 1);
        testKafkaListener(REMOVE, templateId, 0);
    }

    private void testKafkaListener(Operation operation, Long templateId, Integer expectedSize) throws InterruptedException {
        TemplateRecipientKafka persistent_message = TemplateRecipientKafka.builder()
                .operation(operation)
                .templateId(templateId)
                .recipientId(1L)
                .build();

        CompletableFuture<Void> future = kafkaListeners.listener(persistent_message);
        kafkaTemplate.send(consumerTopic, persistent_message);
        Awaitility.await().atMost(Duration.ofSeconds(5)).until(future::isDone);
        Thread.sleep(500L);

        assertThat(recipientIdRepository.findAll()).hasSize(expectedSize);
    }

    private Long createTemplateCreated() throws Exception {
        ResultActions result = mockMvc.perform(post(CREATE.getUrl())
                .header("clientId", CLIENT_ID)
                .content(TEMPLATE.toJson())
                .contentType(APPLICATION_JSON));
        return Long.valueOf(extractJsonValueByKey(result, "id"));
    }

    private void addRecipientsTemplateCreated(Long templateId) throws Exception {
        mockMvc.perform(post(ADD_REC.getUrl().formatted(templateId))
                .header("clientId", CLIENT_ID)
                .content(new RecipientListJson(List.of(1)).toJson())
                .contentType(APPLICATION_JSON));
    }
}
