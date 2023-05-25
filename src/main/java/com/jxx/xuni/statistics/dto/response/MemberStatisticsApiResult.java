package com.jxx.xuni.statistics.dto.response;

public record MemberStatisticsApiResult<T>(
        Integer status,
        String message,
        T response
) {}
