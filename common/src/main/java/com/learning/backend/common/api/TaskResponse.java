package com.learning.backend.common.api;

import java.time.Instant;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
