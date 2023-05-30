package com.jxx.xuni.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select r from Review r " +
            "where r.studyProductId =:studyProductId " +
            "and r.isDeleted = false ")
    List<Review> readBy(@Param("studyProductId") String studyProductId);
}
