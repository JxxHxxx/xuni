package com.jxx.xuni.statistics.dto.response;

public record StudyProductStatisticsReadResponse(
   String studyProductId,
   Integer ratingSum,
   Integer reviewCnt
) {}