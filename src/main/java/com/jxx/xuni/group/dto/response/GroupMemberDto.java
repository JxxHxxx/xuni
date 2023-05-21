package com.jxx.xuni.group.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupMemberDto {
    private final Long groupMemberId;
    private final String groupMemberName;
    private final Boolean isLeft;
    private final LocalDateTime lastVisitedTime;

    public GroupMemberDto(Long groupMemberId, String groupMemberName, Boolean isLeft, LocalDateTime lastVisitedTime) {
        this.groupMemberId = groupMemberId;
        this.groupMemberName = groupMemberName;
        this.isLeft = isLeft;
        this.lastVisitedTime = lastVisitedTime;
    }
}