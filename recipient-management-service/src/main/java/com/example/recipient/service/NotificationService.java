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

import java.util.ArrayList;
import java.util.List;

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

    public static <T> List<List<T>> splitList(List<T> list, int parts) {
        int size = list.size();
        int partitionSize = (int) Math.ceil((double) size / parts);
        List<List<T>> subLists = new ArrayList<>();

        for (int i = 0; i < size; i += partitionSize) {
            int end = Math.min(i + partitionSize, size);
            subLists.add(list.subList(i, end));
        }

        return subLists;
    }
}
