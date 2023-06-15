package com.example.recipient.config;

import com.example.recipient.entity.Client;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.example.recipient.model.Role.USER;

@IT
@Sql({
        "classpath:sql/data.sql"
})
public class ITBase {

    public Client userDetails = Client.builder()
            .id(1L)
            .role(USER)
            .email("authenitcated@gmail.com")
            .build();

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:alpine3.17");

    @BeforeAll
    static void runContainer() {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    }
}
