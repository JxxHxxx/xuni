package com.xuni.api.group.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupApiMessage {

    public static final String GROUP_JOIN = "스터디 그룹에 정상적으로 참여하셨습니다.";
    public static final String GROUP_CLOSE_RECRUITMENT = "스터디 그룹 모집을 마감합니다.";
    public static final String GROUP_CREATED = "스터디가 정상적으로 생성되었습니다.";
    public static final String GROUP_UNCREATED = "스터디 그룹을 생성할 수 없습니다.";

    public static final String GROUP_ALL_READ = "전체 그룹 조회 완료";
    public static final String GROUP_ONE_READ = "그룹 단일 조회 완료";
    public static final String GROUP_CATEGORY_READ = "그룹 카테고리 별 조회 완료";
    public static final String SEARCH_GROUP_COND = "그룹 조건 조회 완료";

    public static final String STUDY_CHECK_OF_GROUP_MEMBER = "그룹 멤버 스터디 투두 리스트 조회 완료";
    public static final String GROUP_OWN_READ = "자신의 일정 읽기 완료";

    public static final String GROUP_START = "그룹 스터디를 시작합니다.";
    public static final String LEAVE_GROUP = "그룹을 떠납니다.";
    public static final String DO_CHECK = "체크 버튼을 누릅니다.";
}
