package com.jxx.xuni.statistics.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticsExceptionMessage {
    public static final String NOT_APPROPRIATE_PROGRESS = "진행률은 0 ~ 100 여야 합니다";

    public static final String NOT_APPROPRIATE_RATING = "평점은 0 ~ 5 여야 합니다.";
    public static final String NOT_APPROPRIATE_REVIEW_CNT = "리뷰 총 수는  0 이상이여야 합니다.";
}