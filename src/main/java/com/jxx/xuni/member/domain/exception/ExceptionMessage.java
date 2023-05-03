package com.jxx.xuni.member.domain.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
    }

    public static final String UNAUTHENTICATED = "인증 코드가 인증되지 않았습니다";
    public static final String INCORRECT_AUTH_CODE_VALUE = "올바르지 않은 인증 코드 값입니다";
}
