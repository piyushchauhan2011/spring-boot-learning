package com.learning.backend.common.api;

public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
