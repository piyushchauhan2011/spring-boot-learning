package com.learning.backend.api.demo;

import com.learning.backend.api.config.AppProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class DemoPartnerClient {

    private final RestClient restClient;

    public DemoPartnerClient(AppProperties appProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(appProperties.getDemoPartner().getBaseUrl())
                .build();
    }

    /**
     * Calls a flaky upstream on purpose; opens the circuit after enough failures.
     */
    @CircuitBreaker(name = "demoPartner", fallbackMethod = "fallbackStatusEndpoint")
    public String fetchStatusEndpoint() {
        return restClient.get()
                .uri("/status/500")
                .retrieve()
                .body(String.class);
    }

    public String fallbackStatusEndpoint(Throwable t) {
        return "{\"degraded\":true,\"reason\":\"" + t.getClass().getSimpleName() + "\"}";
    }
}
