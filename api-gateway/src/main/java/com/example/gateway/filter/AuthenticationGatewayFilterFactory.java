package com.example.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final RouteValidator validator;
    private final RestTemplate restTemplate;

    @Value("${urls.validate}")
    private String validateUrl;

    public AuthenticationGatewayFilterFactory(RouteValidator validator, RestTemplate restTemplate) {
        super(Config.class);
        this.validator = validator;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                ResponseEntity<Long> responseEntity = sendValidationRequest(exchange);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    ServerHttpRequest request = addClientIdHeader(responseEntity.getBody(), exchange);
                    return chain.filter(
                            exchange.mutate()
                                    .request(request)
                                    .build()
                    );
                } else {
                    throw new RuntimeException("Request failed with status code: " + responseEntity.getStatusCode());
                }
            }
            return chain.filter(
                    exchange.mutate()
                            .build()
            );
        };
    }

    public static class Config {
    }

    private ResponseEntity<Long> sendValidationRequest(ServerWebExchange exchange) {
        HttpHeaders headers = new HttpHeaders();
        String jwt = exchange.getRequest().getHeaders().getFirst("Authorization");
        headers.set("Authorization", jwt);
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(validateUrl));
        return restTemplate.exchange(requestEntity, Long.class); // TODO: WebFlux exception handling
    }

    private ServerHttpRequest addClientIdHeader(Long clientId, ServerWebExchange exchange) {
        return exchange.getRequest()
                .mutate()
                .header("clientId", String.valueOf(clientId))
                .build();
    }
}
