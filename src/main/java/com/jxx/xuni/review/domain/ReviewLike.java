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
    private Long reviewId; // Review 간접 참조 키입니다.
    private Long memberId;
    private boolean isLiked;
    @Enumerated(value = EnumType.STRING)
    private LikeStatus likeStatus;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;

    public ReviewLike(Long reviewId, Long memberId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.likeStatus = LikeStatus.INIT;
        this.isLiked = true;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
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
            return;
        }

        if (likeStatus.equals(LikeStatus.DISLIKE)) {
            likeStatus = LikeStatus.LIKE;
        }
    }
}
