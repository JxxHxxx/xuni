package com.jxx.xuni.member.domain.exception;

public class ExceptionMessage {

    private ExceptionMessage() {
    }

    public static final String UNAUTHENTICATED = "인증 코드가 인증되지 않았습니다";
    public static final String INCORRECT_AUTH_CODE_VALUE = "올바르지 않은 인증 코드 값입니다";
    public static final String NOT_EXIST_AUTH_CODE = "존재하지 않는 인증 코드입니다";
    public static final String ALREADY_EXIST_EMAIL = "이미 가입된 이메일입니다";
    public static final String VALID_TIME_OUT = "인증 코드 유효 시간이 초과하였습니다";
}
