package com.xuni.api.review.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.core.common.http.DataResponse;
import com.xuni.core.common.http.SimpleResponse;
import com.xuni.api.review.application.ReviewService;
import com.xuni.api.review.dto.request.ReviewForm;
import com.xuni.api.review.dto.request.ReviewUpdateForm;
import com.xuni.api.review.dto.response.RatingResponse;
import com.xuni.api.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xuni.api.review.dto.response.ReviewApiMessage.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<SimpleResponse> createReview(@RequestBody @Validated ReviewForm form,
                                                              @PathVariable("study-product-id") String studyProductId,
                                                              @AuthenticatedMember MemberDetails memberDetails) {
        Long reviewId = reviewService.create(memberDetails, studyProductId, form);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .headers(httpHeaders -> httpHeaders.add(HttpHeaders.LOCATION, "/reviews/" + reviewId))
                             .body(SimpleResponse.create(REVIEW_CREATE));
    }

    @GetMapping("/study-products/{study-product-id}/reviews")
    public ResponseEntity<DataResponse> readReviewBy(@PathVariable("study-product-id") String studyProductId) {
        List<ReviewOneResponse> response = reviewService.read(studyProductId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new DataResponse(200, REVIEW_READ, response));
    }

    @GetMapping("/study-products/{study-product-id}/rating-avg")
    public ResponseEntity<DataResponse> readRatingAvg(@PathVariable("study-product-id") String studyProductId) {
        RatingResponse response = reviewService.readRatingAvg(studyProductId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new DataResponse(200, RATING_AVG, response));
    }

    @PatchMapping("/reviews/{review-id}")
    public ResponseEntity<SimpleResponse> updateReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId,
                                                              @RequestBody @Validated ReviewUpdateForm form) {
        reviewService.updateReview(reviewId, memberDetails.getUserId(), form);
        return ResponseEntity.status(HttpStatus.OK)
                              .body(new SimpleResponse(200, REVIEW_UPDATE));
    }

    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<SimpleResponse> deleteReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("review-id") Long reviewId) {
        reviewService.deleteReview(reviewId, memberDetails.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SimpleResponse(200 , REVIEW_DELETE));
    }

}
