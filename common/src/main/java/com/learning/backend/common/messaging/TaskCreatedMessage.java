package com.learning.backend.common.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Domain event published when a task is created (at-least-once delivery; consumers must dedupe).
 */
public record TaskCreatedMessage(
        @JsonProperty("taskId") Long taskId,
        @JsonProperty("title") String title,
        @JsonProperty("occurredAt") Instant occurredAt,
        @JsonProperty("messageId") String messageId
) {
}
