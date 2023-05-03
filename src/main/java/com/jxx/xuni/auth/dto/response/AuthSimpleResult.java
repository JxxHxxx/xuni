package com.jxx.xuni.auth.dto.response;

import lombok.Getter;

@Getter
public class AuthSimpleResult {
    private final Integer status;
    private final String message;
    private final LoginResponse response;

    private AuthSimpleResult(Integer status, String message, LoginResponse response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    public static AuthSimpleResult signup() {
        return new AuthSimpleResult(201, "success signup", null);
    }

    public static AuthSimpleResult login(LoginResponse response) {
        return new AuthSimpleResult(200, "welcome xuni", response);
    }

    public static AuthSimpleResult verified() {
        return new AuthSimpleResult(200, "인증되었습니다.", null);
    }

    public static AuthSimpleResult logout() {
        return new AuthSimpleResult(200, "good-bye", null);
    }
}
