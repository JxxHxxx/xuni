package com.jxx.xuni.group.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupApiMessage {
    public static final String GROUP_CREATED = "스터디가 정상적으로 생성되었습니다.";
    public static final String GROUP_UNCREATED = "스터디 생성이 실패했습니다.";
    public static final String GROUP_JOIN = "스터디 그룹에 정상적으로 참여하셨습니다.";
}
