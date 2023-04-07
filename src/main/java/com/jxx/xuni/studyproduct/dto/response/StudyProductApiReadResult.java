package com.jxx.xuni.studyproduct.dto.response;

import lombok.Getter;

@Getter
public class StudyProductApiReadResult<T> {
    private Integer status;
    private String message;
    private T response;

    public StudyProductApiReadResult(String message, T response) {
        this.status = 200;
        this.message = message;
        this.response = response;
    }
}