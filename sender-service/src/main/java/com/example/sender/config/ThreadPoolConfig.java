package com.example.sender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {

    private static final int DEFAULT_POOL_SIZE = 4;

    @Bean
    public ThreadPoolTaskExecutor emailSenderExecutor() {
        return createThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor phoneSenderExecutor() {
        return createThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolTaskExecutor telegramSenderExecutor() {
        return createThreadPoolTaskExecutor();
    }

    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(DEFAULT_POOL_SIZE);
        executor.setMaxPoolSize(DEFAULT_POOL_SIZE * 2);
        executor.setQueueCapacity(DEFAULT_POOL_SIZE * 2);
        executor.setThreadNamePrefix("async-executor-");
        executor.initialize();
        return executor;
    }
}
