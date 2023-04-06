package com.jxx.xuni.group.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupApiMessage {
    public static final String GROUP_CREATED = "스터디가 정상적으로 생성되었습니다.";
    public static final String GROUP_UNCREATED = "스터디 그룹을 생성할 수 없습니다.";
    public static final String Not_APPROPRIATE_GROUP_STATUS = "현재 그룹 상태에서는 해당 요청을 처리할 수 없습니다.";
    public static final String GROUP_JOIN = "스터디 그룹에 정상적으로 참여하셨습니다.";
    public static final String NOT_PERMISSION = "권한이 존재하지 않습니다.";
    public static final String GROUP_CLOSE_RECRUITMENT = "스터디 그룹 모집을 마감합니다.";
}
