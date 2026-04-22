package com.learning.backend.api.demo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@SecurityRequirement(name = "bearerAuth")
public class DemoController {

    private final DemoPartnerClient demoPartnerClient;

    public DemoController(DemoPartnerClient demoPartnerClient) {
        this.demoPartnerClient = demoPartnerClient;
    }

    @GetMapping("/resilience")
    String resilienceProbe() {
        return demoPartnerClient.fetchStatusEndpoint();
    }
}
