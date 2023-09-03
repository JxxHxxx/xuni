package com.xuni.api.review.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.core.common.http.SimpleResponse;
import com.xuni.api.review.application.ReviewLikeService;
import com.xuni.core.review.domain.LikeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.xuni.api.review.dto.response.ReviewApiMessage.*;

@RestController
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    // TODO : 리로스 위치를 URI 통해 식별할 수 없는 경우 CREATE (201) 상태 코드를 응답하는 것이 맞는지 판단
    @PostMapping("/reviews/{review-id}/like")
    public ResponseEntity<SimpleResponse> execute(@AuthenticatedMember MemberDetails memberDetails,
                                                  @PathVariable("review-id") Long reviewId) {

        LikeStatus likeStatus = reviewLikeService.execute(reviewId, memberDetails.getUserId());

        if (likeStatus.equals(LikeStatus.INIT)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SimpleResponse(201, REVIEW_LIKE_CREATE));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SimpleResponse(200, REVIEW_LIKE_UPDATE));
    }
}
