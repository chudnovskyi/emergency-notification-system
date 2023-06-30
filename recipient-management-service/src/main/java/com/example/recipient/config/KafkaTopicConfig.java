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

    @Value("${spring.kafka.topics.notifications.email}")
    private String emailTopic;

    @Value("${spring.kafka.topics.notifications.phone}")
    private String phoneTopic;

    @Value("${spring.kafka.topics.notifications.telegram}")
    private String telegramTopic;

    @Value("${spring.kafka.topics.splitter}")
    private String splitterTopic;

    @Bean
    public NewTopic emailNotificationTopic() {
        return TopicBuilder.name(emailTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic phoneNotificationTopic() {
        return TopicBuilder.name(phoneTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    public NewTopic telegramNotificationTopic() {
        return TopicBuilder.name(telegramTopic)
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
