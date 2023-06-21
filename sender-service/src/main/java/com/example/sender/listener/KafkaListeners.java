package com.example.sender.listener;

import com.example.sender.dto.kafka.RecipientKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private int i = 0;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification}' }",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientKafka recipientList) {
        System.out.println(++i + ": " + recipientList.recipientResponse().email());
    }
}
