package com.xuni.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
    private Integer status;
    private String message;

    private ExceptionResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse of(Integer status, String message) {
        return new ExceptionResponse(status, message);
    }
}
