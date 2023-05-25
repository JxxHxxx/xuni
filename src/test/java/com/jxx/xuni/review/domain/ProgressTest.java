package com.jxx.xuni.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProgressTest {

    @DisplayName("Progress 객체는 진행률에 따라 알맞는 Progress 를 반환한다.")
    @ParameterizedTest(name = "[{index}] {2}")
    @CsvSource(value = {"90, ALMOST, progress 90 이상", "50, HALF, progress 90 미만 50 이상", "1, LIGHTLY, progress 50 미만 1 이상", "0, ZERO, progress 1 미만"})
    void rate(int progress, Progress progressEnum, String message) {
        //when
        Progress rate = Progress.rate(progress);
        //then
        assertThat(rate).isEqualTo(progressEnum);
    }
}