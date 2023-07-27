package com.jxx.xuni.review.domain;

import com.jxx.xuni.review.domain.exception.ReviewDeletedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static com.jxx.xuni.review.domain.LikeStatus.DISLIKE;
import static com.jxx.xuni.review.domain.LikeStatus.INIT;
import static com.jxx.xuni.review.domain.exception.ReviewExceptionMessage.REVIEW_DELETED;
import static org.assertj.core.api.Assertions.*;


class ReviewLikeTest {

    Review review = null;
    Long reviewerId = 100l; /**리뷰를 삭제할 때, 사용됩니다.*/

    @BeforeEach
    void beforeEach() {
        review = Review.builder()
                .reviewer(Reviewer.of(reviewerId, "reviewer1", Progress.HALF))
                .content(Content.of(3, "reviewer's comment"))
                .studyProductId("study-product-id")
                .build();
    }

    @Test
    void init() {
        Long memberId = 1l;
        ReviewLike reviewLike = new ReviewLike(memberId, review);

        assertThat(reviewLike.getLikeStatus()).isEqualTo(INIT);
        assertThat(reviewLike.isLiked()).isTrue();
    }

    @Test
    void execute() {
        //given
        Long memberId = 1l;
        ReviewLike reviewLike = new ReviewLike(memberId, review);
        //when
        reviewLike.execute();
        //then
        assertThat(reviewLike.getReview()).isEqualTo(review);
        assertThat(reviewLike.isLiked()).isFalse();
        assertThat(reviewLike.getLikeStatus()).isEqualTo(DISLIKE);
    }

    @DisplayName("Review 엔티티의 삭제 플래그(isDeleted) 필드가 true 라면 , ReviewLike 를 생성할 수 없다. " +
            "생성 시 ReviewDeletedException 예외" +
            "예외 메시지 REVIEW_DELETED 가 발생한다.")
    @Test
    void construct_fail_cuz_review_deleted() {
        //given
        Long memberId = 1l;
        review.delete(reviewerId);

        //when - then
        assertThatThrownBy(() -> new ReviewLike(memberId, review))
                .isInstanceOf(ReviewDeletedException.class)
                .hasMessage(REVIEW_DELETED);

    }

    @DisplayName("Review 엔티티의 삭제 플래그(isDeleted) 필드가 true 라면 , execute 를 실행할 수 없다." +
            "생성 시 ReviewDeletedException 예외" +
            "예외 메시지 REVIEW_DELETED 가 발생한다.")
    @Test
    void execute_fail_cuz_review_deleted() {
        //given
        Long memberId = 1l;
        ReviewLike reviewLike = new ReviewLike(memberId, review);
        reviewLike.getReview().delete(reviewerId);

        //when - then
        assertThatThrownBy(() -> reviewLike.execute())
                .isInstanceOf(ReviewDeletedException.class)
                .hasMessage(REVIEW_DELETED);
    }

}