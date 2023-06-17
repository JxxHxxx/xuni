package com.jxx.xuni.studyproduct.dto.response;

import lombok.Getter;

@Getter
public class StudyProductApiReadResult<T> {
    private final Integer status;
    private final String message;
    private final T response;

    public StudyProductApiReadResult(String message, T response) {
        this.status = 200;
        this.message = message;
        this.response = response;
    }
}