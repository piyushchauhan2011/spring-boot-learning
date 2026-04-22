package com.learning.backend.common.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KahanSummationTest {

    @Test
    void reducesErrorVersusNaiveSum_forIllConditionedSeries() {
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
        assertThat(kahan).isEqualTo(1.0e16 + (values.length - 1));
        assertThat(naive).isNotEqualTo(kahan);
    }
}
