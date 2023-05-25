package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.common.event.trigger.StatisticsUpdateEvent;
import com.jxx.xuni.review.application.ReviewService;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.response.ReviewApiResult;
import com.jxx.xuni.review.dto.response.ReviewApiSimpleResult;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ApplicationEventPublisher eventPublisher;
    private final ReviewService reviewService;

    @PostMapping("/reviews/study-products/{study-product-id}")
    public ResponseEntity<ReviewApiSimpleResult> createReview(@AuthenticatedMember MemberDetails memberDetails,
                                                              @PathVariable("study-product-id") String studyProductId,
                                                              @RequestBody @Validated ReviewForm form) {
        StatisticsUpdateEvent event = new StatisticsUpdateEvent(memberDetails.getUserId(), studyProductId);
        eventPublisher.publishEvent(event);
        reviewService.create(memberDetails, studyProductId, form);

        return new ResponseEntity<>(ReviewApiSimpleResult.create("리뷰 작성 완료"), CREATED);
    }

    @GetMapping("/reviews/study-products/{study-product-id}")
    public ResponseEntity<ReviewApiResult> readReviewBy(@PathVariable("study-product-id") String studyProductId) {
        List<ReviewOneResponse> response = reviewService.read(studyProductId);

        return ResponseEntity.ok(new ReviewApiResult(200, "리뷰 조회 완료", response));
    }
}
