package com.jxx.xuni.review.dto.response;

public record ReviewApiResult<T>(
        Integer status,
        String message,
        T response
) {
}
