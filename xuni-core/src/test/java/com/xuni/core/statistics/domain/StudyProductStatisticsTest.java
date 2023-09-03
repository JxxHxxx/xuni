package com.xuni.core.statistics.domain;

import com.xuni.core.statistics.domain.StudyProductStatistics;
import com.xuni.core.statistics.domain.exception.RatingOutOfBoundException;
import com.xuni.core.statistics.domain.exception.ReviewCntOutOfBoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.xuni.core.statistics.domain.exception.StatisticsExceptionMessage.NOT_APPROPRIATE_RATING;
import static com.xuni.core.statistics.domain.exception.StatisticsExceptionMessage.NOT_APPROPRIATE_REVIEW_CNT;
import static org.assertj.core.api.Assertions.*;

class StudyProductStatisticsTest {

    @DisplayName("add 메서드 실행 시" +
            "1. 평점 총 합(ratingSum) 필드는 기존 값에서 add 메서드 파라미터(rating) 값을 더한 값으로 변경된다." +
            "2. 리뷰 총 수 필드는 기존 값에서 1 증가한다.")
    @Test
    void add_success() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id("study-product-id")
                .ratingSum(3)
                .reviewCnt(1).build();
        //when
        statistics.add(5);
        //then
        assertThat(statistics.getRatingSum()).isEqualTo(8);
        assertThat(statistics.getReviewCnt()).isEqualTo(2);
    }

    /** 해당 제약 조건은
     * update(rating) rating param,
     * delete(updateRating, preRating) updateRating, preRating parmas 에도 동일하게 적용된다. **/

    @DisplayName("add 메서드의 rating 파라미터는 0 ~ 5 사이의 값을 가져야 한다. 그렇지 않을 경우" +
            "RatingOutOfBoundException 예외가 발생하고" +
            "NOT_APPROPRIATE_RATING 메시지가 반환된다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    void add_fail_cause_out_of_bound_rating(int rating) {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id("study-product-id")
                .ratingSum(3)
                .reviewCnt(1).build();
        //when - then
        assertThatThrownBy(() -> statistics.add(rating))
                .isInstanceOf(RatingOutOfBoundException.class)
                .hasMessage(NOT_APPROPRIATE_RATING);
    }

    @DisplayName("update 메서드 실행 시" +
            "1. 평점 총 합(ratingSum) 필드는 기존 값에서 (updateRating - preRating) 을 더한 값으로 변경된다." +
            "2. 리뷰 총 수 필드는 변경되지 않는다.")
    @Test
    void update_success() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id("study-product-id")
                .ratingSum(3)
                .reviewCnt(1).build();

        statistics.update(1, 5);

        assertThat(statistics.getRatingSum()).isEqualTo(7);
        assertThat(statistics.getReviewCnt()).isEqualTo(1);
    }

    @DisplayName("delete 메서드 실행 시" +
            "1. 평점 총 합(ratingSum) 필드는 기존 값에서 파라미터(rating)를 차감한 값으로 변경된다." +
            "2. 리뷰 총 수 필드는 1 감소한다.")
    @Test
    void delete_success() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id("study-product-id")
                .ratingSum(3)
                .reviewCnt(1).build();
        //when
        statistics.delete(3);
        //then
        assertThat(statistics.getRatingSum()).isEqualTo(0);
        assertThat(statistics.getReviewCnt()).isEqualTo(0);
    }

    @DisplayName("reviewCnt 는 0 이상의 값을 가져야 한다. 만약 delete 메서드 실행으로인해 0 미만의 값을 가질 경우" +
            "ReviewCntOutOfBoundException 예외" +
            "NOT_APPROPRIATE_REVIEW_CNT 메시지를 응답한다.")
    @Test
    void delete_fail_cause_out_of_bound_review_cnt() {
        //given
        StudyProductStatistics statistics = StudyProductStatistics.builder()
                .id("study-product-id")
                .ratingSum(0)
                .reviewCnt(0).build();

        assertThatThrownBy(() -> statistics.delete(3))
                .isInstanceOf(ReviewCntOutOfBoundException.class)
                .hasMessage(NOT_APPROPRIATE_REVIEW_CNT);
    }
}