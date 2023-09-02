package com.xuni.api.review.application;

import com.xuni.api.review.infra.ReviewLikeRepository;
import com.xuni.api.review.infra.ReviewRepository;
import com.xuni.review.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public LikeStatus execute(Long reviewId, Long memberId) {
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.readWithReviewFetch(reviewId, memberId);
        if (optionalReviewLike.isPresent()) {
            ReviewLike reviewLike = optionalReviewLike.get();
            reviewLike.execute();
            return reviewLike.getLikeStatus();
        }

        Review review = reviewRepository.findById(reviewId).get();
        ReviewLike reviewLike = reviewLikeRepository.save(new ReviewLike(memberId, review));
        return reviewLike.getLikeStatus();
    }
}
