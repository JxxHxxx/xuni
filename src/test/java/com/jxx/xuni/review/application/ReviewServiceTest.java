package com.jxx.xuni.review.application;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.review.domain.*;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

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
                .studyProductId("before-each-id").build();

        Review review2 = Review.builder()
                .reviewer(Reviewer.of(2l, "바니", Progress.HALF))
                .content(Content.of((byte) 1, "this book is really bad"))
                .studyProductId("before-each-id").build();
        reviewRepository.saveAll(List.of(review1, review2));
    }

    @DisplayName("리뷰 생성 시, 정상적으로 저장 되어야 한다.")
    @Test
    void create() {
        //given
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        ReviewForm form = new ReviewForm((byte) 4, "this book is really good", 100);
        //when
        reviewService.create(memberDetails, "study-product-id", form);
        //then
        assertThat(reviewRepository.readBy("study-product-id")).isNotNull();
    }

    @DisplayName("리뷰 조회 시 해당 스터디 상품에 달린 댓글 수 만큼 가져온다.")
    @Test
    void read() {
        //when
        List<ReviewOneResponse> responses = reviewService.read("before-each-id");
        //then
        assertThat(responses.size()).isEqualTo(2);
    }
}