package com.jxx.xuni.review.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.jxx.xuni.review.domain.LikeStatus.DISLIKE;
import static com.jxx.xuni.review.domain.LikeStatus.INIT;
import static org.assertj.core.api.Assertions.*;


class ReviewLikeTest {

    Review review;

    @BeforeEach
    void beforeEach() {
        review = Review.builder()
                .reviewer(Reviewer.of(100l, "reviewer1", Progress.HALF))
                .content(Content.of(3, "reviewer's comment"))
                .studyProductId("study-product-id")
                .build();
    }

    @Test
    void init()  {
        Long memberId = 1l;
        ReviewLike reviewLike = new ReviewLike(memberId, review);

        assertThat(reviewLike.getLikeStatus()).isEqualTo(INIT);
        assertThat(reviewLike.isLiked()).isTrue();
    }

    @Test
    void execute() {
        Long memberId = 1l;
        ReviewLike reviewLike = new ReviewLike(memberId, review);

        reviewLike.execute();
        assertThat(reviewLike.getReview()).isEqualTo(review);
        assertThat(reviewLike.isLiked()).isFalse();
        assertThat(reviewLike.getLikeStatus()).isEqualTo(DISLIKE);
    }

}