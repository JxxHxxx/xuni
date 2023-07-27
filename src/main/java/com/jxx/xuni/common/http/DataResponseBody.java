package com.jxx.xuni.common.http;

public record DataResponseBody<T>(
        Integer status,
        String message,
        T response
) {

}