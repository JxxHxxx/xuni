package com.jxx.xuni.auth.dto.response;

import lombok.Getter;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.SIGNUP;

@Getter
public class AuthSimpleResult {
    private final Integer status;
    private final String message;

    private AuthSimpleResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static AuthSimpleResult signup() {
        return new AuthSimpleResult(201, SIGNUP);
    }

}
