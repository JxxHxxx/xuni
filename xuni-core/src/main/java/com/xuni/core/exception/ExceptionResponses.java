package com.xuni.core.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExceptionResponses {
    private Integer status;
    private List<String> message;

    private ExceptionResponses(Integer status, List<String> message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponses of(Integer status, List<String> message) {
        return new ExceptionResponses(status, message);
    }
}
