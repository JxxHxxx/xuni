package com.jxx.xuni.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonExceptionMessage {
    public static final String BAD_REQUEST = "요청이 올바르지 않습니다";
    public static final String FORBIDDEN = "권한이 없습니다";
    public static final String REQUIRED_LOGIN = "로그인이 필요합니다";
}
