package com.example.sender.listener;

import com.example.sender.dto.kafka.RecipientListKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    private int i = 0;

    @KafkaListener(
            topics = "notification",
            groupId = "emergency",
            containerFactory = "listenerContainerFactory"
    )
    private void listener(RecipientListKafka recipientList) {
        System.out.println(++i + ": " + recipientList);
    }
}
