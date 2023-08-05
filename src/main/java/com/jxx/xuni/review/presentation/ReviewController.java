package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.common.http.DataResponse;
import com.jxx.xuni.common.http.SimpleResponse;
import com.jxx.xuni.review.application.ReviewService;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.request.ReviewUpdateForm;
import com.jxx.xuni.review.dto.response.RatingResponse;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jxx.xuni.review.dto.response.ReviewApiMessage.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<SimpleResponse> createReview(@RequestBody @Validated ReviewForm form,
                                                              @PathVariable("study-product-id") String studyProductId,
                                                              @AuthenticatedMember MemberDetails memberDetails) {
        Long reviewId = reviewService.create(memberDetails, studyProductId, form);

        return ResponseEntity.status(CREATED)
                             .headers(httpHeaders -> httpHeaders.add(LOCATION, "/reviews/" + reviewId))
                             .body(SimpleResponse.create(REVIEW_CREATE));
    }

    @GetMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<DataResponse> readReviewBy(@PathVariable("study-product-id") String studyProductId) {
        List<ReviewOneResponse> response = reviewService.read(studyProductId);

        return ResponseEntity.status(OK)
                             .body(new DataResponse(200, REVIEW_READ, response));
    }

    @GetMapping("/study-products/{study-product-id}/rating-avg")
    public ResponseEntity<DataResponse> readRatingAvg(@PathVariable("study-product-id") String studyProductId) {
        RatingResponse response = reviewService.readRatingAvg(studyProductId);

        return ResponseEntity.status(OK)
                             .body(new DataResponse(200, RATING_AVG, response));
    }

    @PatchMapping("/reviews/{review-id}")
    public ResponseEntity<SimpleResponse> updateReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId,
                                                              @RequestBody @Validated ReviewUpdateForm form) {
        reviewService.updateReview(reviewId, memberDetails.getUserId(), form);
        return ResponseEntity.status(OK)
                              .body(new SimpleResponse(200, REVIEW_UPDATE));
    }

    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<SimpleResponse> deleteReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId, memberDetails.getUserId());
        return ResponseEntity.status(OK)
                .body(new SimpleResponse(200 , REVIEW_DELETE));
    }

}
