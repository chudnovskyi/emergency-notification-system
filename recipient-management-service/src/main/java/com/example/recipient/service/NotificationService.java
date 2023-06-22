package com.example.recipient.service;

import com.example.recipient.dto.kafka.RecipientListKafka;
import com.example.recipient.dto.request.RecipientListRequest;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.entity.Client;
import com.example.recipient.util.NodeChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.recipient.util.CollectionUtils.splitList;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, RecipientListKafka> kafkaTemplate;
    private final NodeChecker nodeChecker;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.kafka.topics.splitter}")
    private String notificationTopic;

    public String distributeRecipients(Client client, RecipientListRequest request) {
        List<List<Long>> lists = splitList(request.recipientIds(), nodeChecker.getAmountOfRunningNodes(applicationName));

        TemplateResponse templateResponse = new TemplateResponse("TODO"); // TODO: Template
        for (List<Long> list : lists) {
            kafkaTemplate.send(notificationTopic, new RecipientListKafka(list, templateResponse, client.getId()));
        }

        return "Sent!";
    }
}
