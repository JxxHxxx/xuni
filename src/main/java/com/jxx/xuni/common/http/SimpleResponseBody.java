package com.jxx.xuni.common.http;

public record SimpleResponseBody(
        Integer status,
        String message
) {
    public static SimpleResponseBody create(String message) {
        return new SimpleResponseBody(201, message);
    }

}