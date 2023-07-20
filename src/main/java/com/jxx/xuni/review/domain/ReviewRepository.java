package com.jxx.xuni.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select r from Review r " +
            "where r.studyProductId =:studyProductId " +
            "and r.isDeleted = false ")
    List<Review> readBy(@Param("studyProductId") String studyProductId);

    @Query(value = "select count(rl) from ReviewLike rl " +
            "where rl.reviewId =:reviewId and rl.isLiked = true ")
    Long countReviewLike(Long reviewId);
}
