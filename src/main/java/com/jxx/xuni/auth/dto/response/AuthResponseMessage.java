package com.jxx.xuni.auth.dto.response;

public class AuthResponseMessage {
    private AuthResponseMessage() {
    }
    public static final String LOGIN_FAIL = "아이디 혹은 비밀번호를 잘못 입력하셨습니다.";
    public static final String EXISTED_EMAIL = "이미 존재하는 이메일입니다.";

    public static final String SIGNUP = "회원 가입이 완료되었습니다";
    public static final String LOGIN = "로그인이 완료되었습니다";

    public static final String SEND_AUTH_CODE = "인증 코드 전송이 완료되었습니다";
    public static final String AUTHENTICATE_CODE = "인증 코드 검증이 완료되었습니다";

    public static final String READ_MEMBER_DETAILS = "유저 정보 조회 완료";
}