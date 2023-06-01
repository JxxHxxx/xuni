package com.jxx.xuni.review.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.review.domain.*;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.request.ReviewUpdateForm;
import com.jxx.xuni.review.dto.response.RatingResponse;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingHandler ratingHandler;

    public void create(MemberDetails memberDetails, String studyProductId, ReviewForm form) {
        Review review = Review.builder()
                .reviewer(Reviewer.of(memberDetails.getUserId(), memberDetails.getName(), Progress.rate(form.progress())))
                .studyProductId(studyProductId)
                .content(Content.of(form.rating(), form.comment()))
                .build();

        reviewRepository.save(review);
    }

    public List<ReviewOneResponse> read(String studyProductId) {
        List<Review> reviews = reviewRepository.readBy(studyProductId);

        return reviews.stream().map(r -> new ReviewOneResponse(
                r.getId(),
                r.receiveComment(),
                r.receiveRating(),
                r.getLastModifiedTime(),
                r.receiveReviewerId(),
                r.receiveReviewerName(),
                r.receiveProgress())).toList();
    }

    @Transactional
    public void updateReview(Long reviewId, Long reviewerId, ReviewUpdateForm form) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));
        review.update(reviewerId, form.rating(), form.comment());
    }

    @Transactional
    public void deleteReview(Long reviewId, Long reviewerId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));
        review.delete(reviewerId);
    }

    public RatingResponse readRatingAvg(String studyProductId) {
        List<Review> reviews = reviewRepository.readBy(studyProductId);

        return new RatingResponse(ratingHandler.calculateAvg(reviews));
    }
}
