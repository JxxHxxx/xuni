package com.jxx.xuni.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_like")
@Entity
public class ReviewLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;
    private Long memberId;
    private boolean isLiked;
    @Enumerated(value = EnumType.STRING)
    private LikeStatus likeStatus;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    public ReviewLike(Long memberId, Review review) {
        this.memberId = memberId;
        this.review = review;
        this.likeStatus = LikeStatus.INIT;
        this.isLiked = true;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();

        this.review.onLikeEvent();
    }

    public void execute() {
        lastModifiedTime = LocalDateTime.now();
        changeIsLiked();
        changeLikeStatus();
    }

    private void changeIsLiked() {
        isLiked = !isLiked;
    }

    private void changeLikeStatus() {
        if (likeStatus.equals(LikeStatus.INIT) || likeStatus.equals(LikeStatus.LIKE)) {
            likeStatus = LikeStatus.DISLIKE;
            review.onLikeCancelEvent();
            return;
        }

        if (likeStatus.equals(LikeStatus.DISLIKE)) {
            likeStatus = LikeStatus.LIKE;
            review.onLikeEvent();
        }
    }
}
