package com.jxx.xuni.review.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import static com.jxx.xuni.review.domain.LikeStatus.DISLIKE;
import static com.jxx.xuni.review.domain.LikeStatus.INIT;
import static org.assertj.core.api.Assertions.*;


class ReviewLikeTest {

    @Test
    void init()  {
        Long memberId = 1l;
        Long reviewId = 100l;
        ReviewLike reviewLike = new ReviewLike(reviewId, memberId);

        assertThat(reviewLike.getReviewId()).isEqualTo(100l);
        assertThat(reviewLike.getLikeStatus()).isEqualTo(INIT);
        assertThat(reviewLike.isLiked()).isTrue();
    }

    @Test
    void execute() {
        Long memberId = 1l;
        Long reviewId = 100l;
        ReviewLike reviewLike = new ReviewLike(reviewId, memberId);

        reviewLike.execute();
        Assertions.assertThat(reviewLike.isLiked()).isFalse();
        Assertions.assertThat(reviewLike.getLikeStatus()).isEqualTo(DISLIKE);
    }

}