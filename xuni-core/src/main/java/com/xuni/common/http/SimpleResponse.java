package com.xuni.common.http;

public record SimpleResponse(
        Integer status,
        String message
) {
    public static SimpleResponse create(String message) {
        return new SimpleResponse(201, message);
    }

}