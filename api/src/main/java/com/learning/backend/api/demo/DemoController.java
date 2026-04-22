package com.learning.backend.api.demo;

import com.learning.backend.common.math.KahanSummation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
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

    /**
     * Public demo: same series as {@link com.learning.backend.common.math.KahanSummationTest} — naive {@code double}
     * accumulation loses small terms; compensated summation matches the mathematical result.
     */
    @GetMapping("/kahan")
    @SecurityRequirements
    public KahanSummationDemoResponse kahanSummation() {
        double[] values = new double[10_000];
        values[0] = 1.0e16;
        for (int i = 1; i < values.length; i++) {
            values[i] = 1.0;
        }
        double naive = 0.0;
        for (double v : values) {
            naive += v;
        }
        double kahan = KahanSummation.sum(values);
        double expected = 1.0e16 + (values.length - 1);
        return new KahanSummationDemoResponse(
                expected,
                naive,
                kahan,
                naive == expected,
                kahan == expected,
                "first term 1e16, remaining 9999 terms are 1.0"
        );
    }

    @GetMapping("/resilience")
    String resilienceProbe() {
        return demoPartnerClient.fetchStatusEndpoint();
    }
}
