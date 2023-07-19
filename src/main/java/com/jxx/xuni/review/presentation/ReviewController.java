package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.review.application.ReviewService;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.request.ReviewUpdateForm;
import com.jxx.xuni.review.dto.response.RatingResponse;
import com.jxx.xuni.review.dto.response.ReviewApiResult;
import com.jxx.xuni.review.dto.response.ReviewApiSimpleResult;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jxx.xuni.review.dto.response.ReviewApiMessage.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<ReviewApiSimpleResult> createReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("study-product-id") String studyProductId,
                                                              @RequestBody @Validated ReviewForm form) {

        reviewService.create(memberDetails, studyProductId, form);
        return new ResponseEntity<>(ReviewApiSimpleResult.create(REVIEW_CREATE), CREATED);
    }

    /**
     * TODO : 리뷰가 여러개일 경우 count query를 리뷰 수 만큼 날림
     * 해결책 1. Review 엔티티에 LikeCnt 필드 추가 -> count query를 날리지 않아도 됨, 그러나 좋아요 이벤트가 발생할 때 마다 업데이트 필요
     * 해결책 2. Review <-> ReviewLike 간에 연관관계를 맺고 애플리케이션 레벨에서 좋아요 수를 구함 -> 테이블간에 강한 의존, 메모리 상의 부하 증가 예상
     */
    @GetMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<ReviewApiResult> readReviewBy(@PathVariable("study-product-id") String studyProductId) {
        List<ReviewOneResponse> response = reviewService.read(studyProductId);

        return ResponseEntity.ok(new ReviewApiResult(200, REVIEW_READ, response));
    }

    @GetMapping("/study-products/{study-product-id}/rating-avg")
    public ResponseEntity<ReviewApiResult> readRatingAvg(@PathVariable("study-product-id") String studyProductId) {
        RatingResponse response = reviewService.readRatingAvg(studyProductId);

        return ResponseEntity.ok(new ReviewApiResult(200, RATING_AVG, response));
    }

    @PatchMapping("/reviews/{review-id}")
    public ResponseEntity<ReviewApiSimpleResult> updateReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId,
                                                              @RequestBody @Validated ReviewUpdateForm form) {
        reviewService.updateReview(reviewId, memberDetails.getUserId(), form);
        return new ResponseEntity<>(ReviewApiSimpleResult.update(REVIEW_UPDATE), OK);
    }

    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<ReviewApiSimpleResult> deleteReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId, memberDetails.getUserId());
        return new ResponseEntity<>(ReviewApiSimpleResult.update(REVIEW_DELETE), OK);
    }

}
