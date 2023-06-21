package com.example.recipient.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.partitions}")
    private Integer partitions;

    @Value("${spring.kafka.topics.notification}")
    private String notificationTopic;

    @Value("${spring.kafka.topics.splitter}")
    private String splitterTopic;

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name(notificationTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic splitterTopic() {
        return TopicBuilder.name(splitterTopic)
                .partitions(partitions)
                .build();
    }
}
