package com.learning.backend.api.demo;

/**
 * JSON view of naive vs Kahan summation on an ill-conditioned series (large magnitude + many small terms).
 */
public record KahanSummationDemoResponse(
        double expected,
        double naiveSum,
        double kahanSum,
        boolean naiveMatchesExpected,
        boolean kahanMatchesExpected,
        String seriesDescription
) {
}
