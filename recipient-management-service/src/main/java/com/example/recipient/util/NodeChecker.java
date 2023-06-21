package com.example.recipient.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NodeChecker {

    private final DiscoveryClient client;
    private final Map<String, Integer> serviceCounts = new HashMap<>();

    public Integer getAmountOfRunningNodes(String serviceName) {
        serviceCounts.putIfAbsent(serviceName, getAmountOfInstances(serviceName));
        return serviceCounts.get(serviceName);
    }

    @Scheduled(fixedDelay = 5000)
    private void monitorService() {
        if (!serviceCounts.isEmpty()) {
            serviceCounts.replaceAll((serviceName, dummy) -> getAmountOfInstances(serviceName));
        }
    }

    private Integer getAmountOfInstances(String serviceName) {
        return client.getInstances(serviceName).size();
    }
}
