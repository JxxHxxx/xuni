package com.jxx.xuni.review.application;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.review.domain.*;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.request.ReviewUpdateForm;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class ReviewServiceTest extends ServiceCommon {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    void beforeEach() {
        Review review1 = Review.builder()
                .reviewer(Reviewer.of(1l, "유니", Progress.HALF))
                .content(Content.of((byte) 5, "this book is really good"))
                .studyProductId("update-each-id").build();

        Review review2 = Review.builder()
                .reviewer(Reviewer.of(2l, "바니", Progress.HALF))
                .content(Content.of((byte) 1, "this book is really bad"))
                .studyProductId("before-each-id").build();

        Review review3 = Review.builder()
                .reviewer(Reviewer.of(3l, "바니", Progress.HALF))
                .content(Content.of((byte) 1, "this book is really bad"))
                .studyProductId("before-each-id").build();
        review3.delete(3l);
        reviewRepository.saveAll(List.of(review1, review2, review3));
    }

    @DisplayName("리뷰 생성 시, 정상적으로 저장 되어야 한다.")
    @Test
    void create_review() {
        //given
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        ReviewForm form = new ReviewForm((byte) 4, "this book is really good", 100);
        //when
        reviewService.create(memberDetails, "study-product-id", form);
        //then
        assertThat(reviewRepository.readBy("study-product-id")).isNotNull();
    }

    @DisplayName("리뷰 조회 시 " +
            "삭제된 댓글을 제외한 " +
            "해당 스터디 상품에 달린 댓글 수 만큼 가져온다.")
    @Test
    void read_review() {
        //given - beforeEach 를 통해 총 3개의 댓글을 넣었고 그 중 1개의 댓글은 삭제된 상태이다
        //when
        List<ReviewOneResponse> responses = reviewService.read("before-each-id");
        //then
        assertThat(responses.size()).isEqualTo(1);
    }

    @DisplayName("리뷰 수정 시 수정한 내용으로 변경된다.")
    @Test
    void update_review() {
        //given 해당 리뷰의 reviewer ID 는 1이다.
        List<Review> reviews = reviewRepository.readBy("update-each-id");
        Long reviewId = reviews.get(0).getId();
        //when - 해당 리뷰의 기존 content 값 Rating 5 | Comment "this book is really good"
        reviewService.updateReview(reviewId, 1l, new ReviewUpdateForm((byte) 3, "this book is really excellent"));
        //then
        Review updateReview = reviewRepository.readBy("update-each-id").get(0);
        assertThat(updateReview.receiveComment()).isEqualTo("this book is really excellent");
        assertThat(updateReview.receiveRating()).isEqualTo((byte) 3);
    }

    @DisplayName("존재하지 않는 리뷰를 수정하려는 경우 " +
            "IllegalArgumentException 예외 NOT_EXIST_ENTITY 예외 메시지 반환")
    @Test
    void update_review_fail_cause_not_exist() {
        Long notExistReviewId = 100l;
        ReviewUpdateForm form = new ReviewUpdateForm((byte) 3, "this book is really excellent");

        assertThatThrownBy(() -> reviewService.updateReview(notExistReviewId, 1l, form))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_ENTITY);
    }

    @DisplayName("리뷰가 정상적으로 삭제될 경우 조회 대상에서 제외된다.")
    @Test
    void delete_review() {
        //given 해당 리뷰의 reviewer ID 는 1 이다.
        List<Review> reviews = reviewRepository.readBy("update-each-id");
        Long reviewId = reviews.get(0).getId();
        //when
        reviewService.deleteReview(reviewId, 1l);
        //then
        List<Review> updateReviews = reviewRepository.readBy("update-each-id");
        assertThat(updateReviews).isEmpty();
    }

    @DisplayName("존재하지 않는 리뷰를 삭제하려는 경우 " +
            "IllegalArgumentException 예외 NOT_EXIST_ENTITY 예외 메시지 반환")
    @Test
    void delete_review_fail_cause_not_exist() {
        Long notExistReviewId = 100l;

        assertThatThrownBy(() -> reviewService.deleteReview(notExistReviewId, 1l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_ENTITY);
    }
}