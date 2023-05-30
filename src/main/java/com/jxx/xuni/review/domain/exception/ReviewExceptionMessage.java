package com.jxx.xuni.review.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewExceptionMessage {
    public static final String NOT_APPROPRIATE_PROGRESS = "진행률은 0 ~ 100 여야 합니다";

}
