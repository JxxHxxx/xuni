package com.xuni.api.review.application;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.review.infra.ReviewRepository;
import com.xuni.common.event.trigger.statistics.ReviewCreatedEvent;
import com.xuni.common.event.trigger.statistics.ReviewDeletedEvent;
import com.xuni.common.event.trigger.statistics.ReviewUpdatedEvent;
import com.xuni.api.review.dto.request.ReviewForm;
import com.xuni.api.review.dto.request.ReviewUpdateForm;
import com.xuni.api.review.dto.response.RatingResponse;
import com.xuni.api.review.dto.response.ReviewOneResponse;
import com.xuni.review.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingHandler ratingHandler;
    private final ApplicationEventPublisher eventPublisher;

    public Long create(MemberDetails memberDetails, String studyProductId, ReviewForm form) {
        Review review = Review.builder()
                .reviewer(Reviewer.of(memberDetails.getUserId(), memberDetails.getName(), Progress.rate(form.progress())))
                .studyProductId(studyProductId)
                .content(Content.of(form.rating(), form.comment()))
                .build();

        Review saveReview = reviewRepository.save(review);

        ReviewCreatedEvent event = new ReviewCreatedEvent(studyProductId, saveReview.receiveRating());
        eventPublisher.publishEvent(event);
        return saveReview.getId();
    }

    public List<ReviewOneResponse> read(String studyProductId) {
        List<Review> reviews = reviewRepository.readBy(studyProductId);

        return reviews.stream().map(review -> new ReviewOneResponse(
                review.getId(),
                review.receiveComment(),
                review.receiveRating(),
                review.getLastModifiedTime(),
                review.receiveReviewerId(),
                review.receiveReviewerName(),
                review.receiveProgress(),
                review.getLikeTotalCnt())).toList();
    }

    @Transactional
    public void updateReview(Long reviewId, Long reviewerId, ReviewUpdateForm form) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));
        Integer ratingBeforeUpdate = review.receiveRating();

        review.update(reviewerId, form.rating(), form.comment());

        ReviewUpdatedEvent event = new ReviewUpdatedEvent(review.getStudyProductId(), ratingBeforeUpdate, form.rating());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long reviewerId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));
        review.delete(reviewerId);

        ReviewDeletedEvent event = new ReviewDeletedEvent(review.getStudyProductId(), review.receiveRating());
        eventPublisher.publishEvent(event);
    }

    public RatingResponse readRatingAvg(String studyProductId) {
        List<Review> reviews = reviewRepository.readBy(studyProductId);

        return new RatingResponse(ratingHandler.calculateAvg(reviews));
    }
}
