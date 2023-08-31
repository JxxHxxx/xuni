package com.xuni.review.domain;

import com.xuni.common.exception.NotPermissionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.xuni.common.exception.CommonExceptionMessage.PRIVATE_ACCESSIBLE;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReviewTest {

    @DisplayName("리뷰 초기화 시" +
            "isDeleted는 false로 초기화되고 ," +
            "lastModifiedTime 은 null 이 아니다.")
    @Test
    void init() {
        Review review = Review.builder()
                .reviewer(Reviewer.of(1l, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        assertThat(review.getIsDeleted()).isFalse();
        assertThat(review.getLastModifiedTime()).isNotNull();
    }

    @DisplayName("리뷰 수정을 정상적으로 성공할 경우, " +
            "rating, comment 는 파라미터로 들어온 값으로 변경된다.")
    @Test
    void update_success_case_1() {
        Long reviewerId = 1l;

        Review review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        review.update(reviewerId, 3, "정말 재미없어요");

        assertThat(review.receiveRating()).isEqualTo(3);
        assertThat(review.receiveComment()).isEqualTo("정말 재미없어요");
    }

    @DisplayName("null 값이 들어올 경우 rating, comment는 그대로 유지된다.")
    @Test
    void update_success_case_2() {
        Long reviewerId = 1l;

        Review review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        review.update(reviewerId, null, null);

        assertThat(review.receiveRating()).isEqualTo(5);
        assertThat(review.receiveComment()).isEqualTo("정말 재밋어요");
    }



    @DisplayName("작성자 자신이 아닐 경우 수정을 할 수 없다. " +
            "수정을 시도할 경우 " +
            "NotPermissionException 예외와 함께 PRIVATE_ACCESSIBLE 메시지를 반환한다.")
    @Test
    void update_fail_cause_not_myself() {
        Long reviewerId = 1l;
        Long anotherUserId = 50l;

        Review review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        assertThatThrownBy(() -> review.update(anotherUserId, 3, "정말 재미없어요"))
                .isInstanceOf(NotPermissionException.class)
                .hasMessage(PRIVATE_ACCESSIBLE);
    }

    @DisplayName("리뷰 삭제를 성공할 경우" +
            "isDeleted 필드가 true 값으로 변경된다.")
    @Test
    void delete_success() {
        Long reviewerId = 1l;
        Review review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        review.delete(reviewerId);

        assertThat(review.getIsDeleted()).isTrue();
    }

    @DisplayName("작성자 자신이 아닐 경우 삭제를 할 수 없다. " +
            "삭제를 시도할 경우 " +
            "NotPermissionException 예외와 함께 PRIVATE_ACCESSIBLE 메시지를 반환한다.")
    @Test
    void delete_fail_cause_not_myself() {
        Long reviewerId = 1l;
        Long anotherUserId = 50l;
        Review review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "wogjs", Progress.HALF))
                .content(Content.of(5, "정말 재밋어요"))
                .studyProductId("study-product-id").build();

        assertThatThrownBy(() -> review.delete(anotherUserId))
                .isInstanceOf(NotPermissionException.class)
                .hasMessage(PRIVATE_ACCESSIBLE);
    }
}