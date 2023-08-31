package com.xuni.api.statistics.dto.response;

public record StudyProductStatisticsReadResponse(
   String studyProductId,
   Integer ratingSum,
   Integer reviewCnt
) {}