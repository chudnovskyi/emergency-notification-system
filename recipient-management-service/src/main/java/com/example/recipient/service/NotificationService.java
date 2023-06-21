package com.example.recipient.service;

import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.TemplateIdListRequest;
import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.RecipientResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.exception.RecipientNotFoundException;
import com.example.recipient.util.NodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${spring.kafka.partitions}")
    private Integer partitions;

    private final NodeChecker nodeChecker;
    private final RecipientService recipientService;
    private final KafkaTemplate<String, RecipientListKafka> kafkaTemplate;

    public String distributeRecipients(Client client, TemplateIdListRequest request) {
        int errors = 0;
        List<RecipientResponse> recipientResponses = new ArrayList<>();
        for (Long recipientId : request.recipientIds()) {
            try {
                RecipientResponse recipient = recipientService.receive(client, recipientId);
                recipientResponses.add(recipient);
            } catch (RecipientNotFoundException e) {
                errors++; // TODO: Reactive Spring
            }
            Integer amountOfRunningNodes = nodeChecker.getAmountOfRunningNodes("sender-service") == 0 ? 1 : nodeChecker.getAmountOfRunningNodes("sender-service");
            int formula = request.recipientIds().size() / (partitions * amountOfRunningNodes);
            if (recipientResponses.size() == formula) {
                sendToKafka("notification", recipientResponses, "!text!");
                recipientResponses.clear();
            }
        }
        if (recipientResponses.size() > 0) {
            sendToKafka("notification", recipientResponses, "!text!");
        }
        return errors == 0 ? "Success!" : "Errors: " + errors;
    }

    // TODO: template logic
    private void sendToKafka(String topic, List<RecipientResponse> recipientResponses, String template) {
        kafkaTemplate.send(
                topic,
                new RecipientListKafka(recipientResponses, new TemplateRequest(template))
        );
        System.out.printf("*** %d requests to %d services\n", recipientResponses.size(), nodeChecker.getAmountOfRunningNodes("sender-service"));
    }
}
