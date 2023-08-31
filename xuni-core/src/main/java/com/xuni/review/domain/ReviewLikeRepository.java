package com.xuni.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);

//    @Query("select rl from ReviewLike rl " +
//            "join fetch rl.review r " +
//            "where rl.review =:reviewId and rl.memberId =:memberId ")
    @Query("select rl from ReviewLike rl " +
            "join fetch rl.review r " +
            "where r.id =:reviewId and rl.memberId =:memberId ")
    Optional<ReviewLike> readWithReviewFetch(Long reviewId, Long memberId);
}
