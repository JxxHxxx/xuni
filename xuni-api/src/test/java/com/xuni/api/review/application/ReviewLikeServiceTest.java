package com.xuni.api.review.application;

import com.xuni.api.support.ServiceCommon;
import com.xuni.api.support.ServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ReviewLikeServiceTest extends ServiceCommon {

    @Autowired
    ReviewLikeService reviewLikeService;
    @Autowired
    ReviewLikeRepository reviewLikeRepository;
    @Autowired
    ReviewRepository reviewRepository;

    Review review;

    @BeforeEach
    void beforeEach() {
        review = reviewRepository.save(Review.builder()
                .reviewer(Reviewer.of(100l, "reviewer1", Progress.HALF))
                .content(Content.of(3, "reviewer's comment"))
                .studyProductId("study-product-id")
                .build());
    }

    @AfterEach
    void afterEach() {
        reviewLikeRepository.deleteAll();
    }

    @DisplayName("리뷰 좋아요를 처음 누르면 좋아요 상태를 나타내는 isLike 필드 값이 True로 설정된다.")
    @Test
    void execute_first_time() {
        //given
        Long reviewId = review.getId();
        Long memberId = 1141l;
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isTrue();
    }

    @DisplayName("이미 좋아요를 누른 상태에서 좋아요를 실행할 경우 isLike 필드 값이 False로 변경된다.")
    @Test
    void execute_already_exist_and_liked_state_true() {
        //given
        Long reviewId = review.getId();
        Long memberId = 1141l;
        reviewLikeRepository.save(new ReviewLike(memberId, review));
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isFalse();
    }

    @DisplayName("이미 좋아요를 취소한 상태에서 다시 좋아요를 누른 경우 다시 True로 변경된다.")
    @Test
    void execute_already_exist_and_liked_state_false() {
        //given
        Long reviewId = review.getId();
        Long memberId = 1141l;
        reviewLikeRepository.save(new ReviewLike(memberId, review));
        reviewLikeService.execute(reviewId, memberId); // isLike 필드 -> false 로 변경
        //when
        reviewLikeService.execute(reviewId, memberId);
        //then
        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId).get();
        assertThat(reviewLike.isLiked()).isTrue();
    }
}