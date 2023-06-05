package com.jxx.xuni.statistics.dto.response;

public record StatisticsApiResult<T>(
        Integer status,
        String message,
        T response
) {}
