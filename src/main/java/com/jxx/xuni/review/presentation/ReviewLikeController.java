package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.review.application.ReviewLikeService;
import com.jxx.xuni.review.domain.LikeStatus;
import com.jxx.xuni.review.dto.response.ReviewApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jxx.xuni.review.dto.response.ReviewApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/reviews/{review-id}/like")
    public ResponseEntity<ReviewApiSimpleResult> execute(@AuthenticatedMember MemberDetails memberDetails,
                                                         @PathVariable("review-id") Long reviewId) {

        LikeStatus likeStatus = reviewLikeService.execute(reviewId, memberDetails.getUserId());

        if (likeStatus.equals(LikeStatus.INIT)) {
            return new ResponseEntity<>(ReviewApiSimpleResult.create(REVIEW_LIKE_CREATE), CREATED);
        }

        return ResponseEntity.ok(ReviewApiSimpleResult.update(REVIEW_LIKE_UPDATE));
    }
}
