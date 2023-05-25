package com.jxx.xuni.review.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.review.domain.*;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

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
}
