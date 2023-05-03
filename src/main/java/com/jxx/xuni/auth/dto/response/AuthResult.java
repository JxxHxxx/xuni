package com.jxx.xuni.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthResult<T> {
    private final Integer status;
    private final String message;
    private final T response;

    public AuthResult(Integer status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }
}
