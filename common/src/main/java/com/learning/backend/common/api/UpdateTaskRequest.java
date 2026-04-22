package com.learning.backend.common.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 10_000) String description
) {
}
