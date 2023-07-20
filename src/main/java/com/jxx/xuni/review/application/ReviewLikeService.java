package com.jxx.xuni.review.application;

import com.jxx.xuni.review.domain.LikeStatus;
import com.jxx.xuni.review.domain.ReviewLike;
import com.jxx.xuni.review.domain.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public LikeStatus execute(Long reviewId, Long memberId) {
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findByReviewIdAndMemberId(reviewId, memberId);
        if (optionalReviewLike.isPresent()) {
            ReviewLike reviewLike = optionalReviewLike.get();
            reviewLike.execute();
            return reviewLike.getLikeStatus();
        }

        ReviewLike reviewLike = new ReviewLike(reviewId, memberId);
        ReviewLike savedReviewLike = reviewLikeRepository.save(reviewLike);
        return savedReviewLike.getLikeStatus();
    }
}
