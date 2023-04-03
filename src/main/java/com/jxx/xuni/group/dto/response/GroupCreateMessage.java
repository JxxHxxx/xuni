package com.jxx.xuni.group.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupCreateMessage {
    public static final String SUCCESS_MESSAGE = "스터디가 정상적으로 생성되었습니다.";
    public static final String FAIL_MESSAGE = "스터디 생성이 실패했습니다.";
}
