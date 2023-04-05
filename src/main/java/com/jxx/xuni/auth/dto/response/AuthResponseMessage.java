package com.jxx.xuni.auth.dto.response;

public class AuthResponseMessage {
    private AuthResponseMessage() {
    }
    public static final String LOGIN_FAIL = "아이디 혹은 비밀번호를 잘못 입력하셨습니다.";
    public static final String EXISTED_EMAIL = "이미 존재하는 이메일입니다.";
}