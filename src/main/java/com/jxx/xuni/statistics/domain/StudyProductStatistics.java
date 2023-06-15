package com.jxx.xuni.statistics.domain;

import com.jxx.xuni.statistics.domain.exception.RatingOutOfBoundException;
import com.jxx.xuni.statistics.domain.exception.ReviewCntOutOfBoundException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.jxx.xuni.statistics.domain.exception.StatisticsExceptionMessage.NOT_APPROPRIATE_RATING;
import static com.jxx.xuni.statistics.domain.exception.StatisticsExceptionMessage.NOT_APPROPRIATE_REVIEW_CNT;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProductStatistics {

    @Id @Column(name = "study_product_statistics_id")
    private String id;
    private Integer ratingSum ;
    private Integer reviewCnt ;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;

    @Builder
    private StudyProductStatistics(String id, Integer ratingSum, Integer reviewCnt) {
        this.id = id;
        this.ratingSum = ratingSum;
        this.reviewCnt = reviewCnt;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void add(Integer rating) {
        checkRatingRange(rating);

        this.ratingSum += rating;
        this.reviewCnt += 1;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void update(Integer preRating, Integer updatedRating) {
        checkRatingRange(preRating);
        checkRatingRange(updatedRating);

        this.ratingSum += updatedRating - preRating;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void delete(Integer rating) {
        checkRatingRange(rating);

        this.ratingSum -= rating;
        this.reviewCnt -= 1;
        this.lastModifiedTime = LocalDateTime.now();

        reviewCntRange();
    }

    private void checkRatingRange(Integer rating) {
        if (rating > 5 || rating < 0) throw new RatingOutOfBoundException(NOT_APPROPRIATE_RATING);
    }

    private void reviewCntRange() {
        if (this.reviewCnt < 0) throw new ReviewCntOutOfBoundException(NOT_APPROPRIATE_REVIEW_CNT);
    }
}
