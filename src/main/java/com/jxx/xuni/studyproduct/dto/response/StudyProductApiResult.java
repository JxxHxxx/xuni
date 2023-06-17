package com.jxx.xuni.studyproduct.dto.response;

import lombok.Getter;

@Getter
public class StudyProductApiResult<T> {
    private final Integer status;
    private final String message;
    private final T response;

    public StudyProductApiResult(Integer status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }
}
