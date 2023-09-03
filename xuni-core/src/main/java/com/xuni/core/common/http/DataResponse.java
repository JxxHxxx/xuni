package com.xuni.core.common.http;

public record DataResponse<T>(
        Integer status,
        String message,
        T response
) {

}