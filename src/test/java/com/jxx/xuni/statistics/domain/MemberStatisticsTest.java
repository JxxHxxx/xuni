package com.jxx.xuni.statistics.domain;


import com.jxx.xuni.statistics.domain.exception.ProgressOutOfBoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.jxx.xuni.statistics.MemberStatisticsSupporter.getBasicMemberStatistics;
import static com.jxx.xuni.statistics.domain.exception.MemberStatisticsExceptionMessage.NOT_APPROPRIATE_PROGRESS;


class MemberStatisticsTest {

    @DisplayName("진행률은 0 미만 그리고 100 초과 값을 가질 수 없다. 범위 내 존재하지 않은 값으로 인스턴스 생성을 시도할 경우 " +
            "ProgressOutOfBoundException 예외" +
            "NOT_APPROPRIATE_PROGRESS 메시지가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void init_fail_cause_out_of_bound_progress(int progress) {
        Assertions.assertThatThrownBy(() -> MemberStatistics.builder()
                .memberId(1l)
                .studyProductId("study-product-identifier")
                .progress(progress).build())
                .isInstanceOf(ProgressOutOfBoundException.class)
                .hasMessage(NOT_APPROPRIATE_PROGRESS);
    }

    @DisplayName("진행률이 올바른 값 범위(0 ~ 100) 내에 존재한다면 기존의 값에 상관없이 해당 값으로 변경된다")
    @ParameterizedTest
    @ValueSource(ints = {0, 50, 100})
    void update_progress_success(int progress) {
        MemberStatistics statistics = getBasicMemberStatistics();

        statistics.updateProgress(progress);
        Assertions.assertThat(statistics.getProgress()).isEqualTo(progress);
    }

    @DisplayName("진행률이 올바른 값 범위 내에 존재하지 않는다면 " +
            "ProgressOutOfBoundException 예외" +
            "NOT_APPROPRIATE_PROGRESS 메시지가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {-1, 101})
    void update_progress_fail_cause_out_of_bound_progress(int progress) {
        MemberStatistics statistics = getBasicMemberStatistics();

        Assertions.assertThatThrownBy(() -> statistics.updateProgress(progress))
                .isInstanceOf(ProgressOutOfBoundException.class)
                .hasMessage(NOT_APPROPRIATE_PROGRESS);
    }
}