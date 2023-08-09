package com.jxx.xuni.common.http;

public record DataResponse<T>(
        Integer status,
        String message,
        T response
) {

}