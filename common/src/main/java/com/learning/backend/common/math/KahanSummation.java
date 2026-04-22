package com.learning.backend.common.math;

/**
 * Compensated summation for double arrays (interview talking point vs {@link java.math.BigDecimal} for money).
 */
public final class KahanSummation {

    private KahanSummation() {
    }

    public static double sum(double[] values) {
        double sum = 0.0;
        double c = 0.0;
        for (double v : values) {
            double y = v - c;
            double t = sum + y;
            c = (t - sum) - y;
            sum = t;
        }
        return sum;
    }
}
