package com.xuni.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonExceptionMessage {
    public static final String BAD_REQUEST = "요청이 올바르지 않습니다";
    public static final String PRIVATE_ACCESSIBLE = "본인만 접근 가능합니다";
    public static final String REQUIRED_LOGIN = "로그인이 필요합니다";
    public static final String ONLY_ADMIN = "관리자에게 요청하십시오";

    public static final String NOT_EXIST_ENTITY = "해당 조건을 만족하는 엔티티가 존재하지 않습니다";
    public static final String EMPTY_VALUE = "값이 비어있거나 NULL 입니다.";
}
