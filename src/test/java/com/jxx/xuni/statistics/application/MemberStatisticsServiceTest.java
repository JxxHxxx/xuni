package com.jxx.xuni.statistics.application;

import com.jxx.xuni.statistics.domain.MemberStatistics;
import com.jxx.xuni.statistics.domain.MemberStatisticsRepository;
import com.jxx.xuni.statistics.dto.response.ReviewNeedResponse;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;
import static org.assertj.core.api.Assertions.*;


@ServiceTest
class MemberStatisticsServiceTest extends ServiceCommon {

    @Autowired
    MemberStatisticsService memberStatisticsService;
    @Autowired
    MemberStatisticsRepository memberStatisticsRepository;

    @BeforeEach
    void beforeEach() {
        MemberStatistics statistics = MemberStatistics.builder()
                .memberId(1l)
                .studyProductId("sp-id")
                .progress(10).build();

        memberStatisticsRepository.save(statistics);
    }

    @DisplayName("findReviewNeedStatistics 성공 시 Review 작성에 필요한 진행률(progress)을 담는다.")
    @Test
    void find_review_need_statistics_success() {
        ReviewNeedResponse response = memberStatisticsService.findReviewNeedStatistics(1l, "sp-id");
        assertThat(response.progress()).isEqualTo(10);
    }

    @DisplayName("멤버, 상품 식별자 중 하나라도 존재하지 않는다면 ")
    @ParameterizedTest(name = "[{index}] : {2}")
    @CsvSource(value = {"999999, sp-id, memberId 존재하지 않은 경우", "1, not-exist-id, studyProductId 존재하지 않은 경우"})
    void find_review_need_statistics_fail_cause_not_exist(long memberId, String studyProductId, String message) {
        assertThatThrownBy(() -> memberStatisticsService.findReviewNeedStatistics(memberId, studyProductId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_ENTITY);
    }
}