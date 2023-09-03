package com.xuni.core.group.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupMessage {
    public static final String NOT_APPROPRIATE_GROUP_STATUS = "현재 그룹 상태에서는 해당 요청을 처리할 수 없습니다.";
    public static final String NOT_APPROPRIATE_GROUP_CAPACITY = "그룹 정원은 최소 1명, 최대 20명입니다.";
    public static final String CURRICULUM_REQUIRED = "커리큘럼은 필수입니다.";
    public static final String NOT_PERMISSION = "권한이 존재하지 않습니다.";
    public static final String ALREADY_JOIN = "이미 들어가 있습니다.";
    public static final String NOT_LEFT_CAPACITY = "남은 자리가 없습니다.";
    public static final String NOT_ACCESSIBLE_GROUP = "입장 가능한 그룹이 아닙니다.";
    public static final String NOT_EXISTED_GROUP = "존재하지 않는 그룹입니다.";
    public static final String NOT_EXISTED_GROUP_MEMBER = "해당 사용자는 그룹 멤버가 아닙니다.";
}
