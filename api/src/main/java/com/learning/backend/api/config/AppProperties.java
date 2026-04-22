package com.learning.backend.api.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final DemoPartner demoPartner = new DemoPartner();

    public Jwt getJwt() {
        return jwt;
    }

    public DemoPartner getDemoPartner() {
        return demoPartner;
    }

    public static class Jwt {
        /**
         * HS256 secret (at least 256 bits / 32 bytes). Dev default is unsafe; override in prod.
         */
        @NotBlank
        private String secret = "dev-dev-dev-dev-dev-dev-dev-dev-dev-dev-dev-dev-32b";

        @Positive
        private long expirationSeconds = 3600;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpirationSeconds() {
            return expirationSeconds;
        }

        public void setExpirationSeconds(long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
        }
    }

    public static class DemoPartner {
        /**
         * Optional HTTP endpoint used to exercise Resilience4j (circuit breaker + fallback).
         */
        private String baseUrl = "https://httpbin.org";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
