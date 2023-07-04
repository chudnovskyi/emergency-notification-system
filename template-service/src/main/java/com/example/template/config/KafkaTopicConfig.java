package com.example.template.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.partitions}")
    private Integer partitions;

    @Value("${spring.kafka.topics.recipient-update}")
    private String recipientUpdateTopic;

    @Bean
    public NewTopic recipientUpdateTopic() {
        return TopicBuilder.name(recipientUpdateTopic)
                .partitions(partitions)
                .build();
    }
}
