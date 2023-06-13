package com.example.recipient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RecipientManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipientManagementServiceApplication.class, args);
    }
}
