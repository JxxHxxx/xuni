package com.jxx.xuni.review.dto.response;

public record ReviewApiSimpleResult(
        Integer status,
        String message
){
    public static ReviewApiSimpleResult create(String message) {
        return new ReviewApiSimpleResult(201, message);
    }
}
