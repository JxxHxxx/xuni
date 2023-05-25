package com.jxx.xuni.review.dto.response;

import com.jxx.xuni.review.domain.Progress;

import java.time.LocalDateTime;

public record ReviewOneResponse(
        Long reviewId,
        String comment,
        Byte rating,
        LocalDateTime lastModifiedTime,
        Long reviewerId,
        String reviewerName,
        Progress progress
) {
}
