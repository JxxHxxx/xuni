package com.jxx.xuni.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_product_review")
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    @Embedded
    private Reviewer reviewer;
    private String studyProductId;
    @Embedded
    private Content content;
    private LocalDateTime lastModifiedTime;

    @Builder
    public Review(Reviewer reviewer, String studyProductId, Content content) {
        this.reviewer = reviewer;
        this.studyProductId = studyProductId;
        this.content = content;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public Byte receiveRating() {
        return this.content.getRating();
    }

    public String receiveComment() {
        return this.content.getComment();
    }

    public Long receiveReviewerId() {
        return this.reviewer.getId();
    }

    public Progress receiveProgress() {
        return this.reviewer.getProgress();
    }

    public String receiveReviewerName() {
        return this.reviewer.getName();
    }

}
