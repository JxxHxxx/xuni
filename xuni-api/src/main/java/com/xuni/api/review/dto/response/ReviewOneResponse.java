package com.xuni.api.review.dto.response;

import com.xuni.review.domain.Progress;

import java.time.LocalDateTime;

public record ReviewOneResponse(
        Long reviewId,
        String comment,
        Integer rating,
        LocalDateTime lastModifiedTime,
        Long reviewerId,
        String reviewerName,
        Progress progress,
        Integer likeCnt
) {
}
