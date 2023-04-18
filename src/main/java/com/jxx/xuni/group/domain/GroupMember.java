package com.jxx.xuni.group.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember {

    private Long groupMemberId;
    private String groupMemberName;
    private Boolean isLeft;

    public GroupMember(Long groupMemberId, String groupMemberName) {
        this.groupMemberId = groupMemberId;
        this.groupMemberName = groupMemberName;
        this.isLeft = false;
    }

    protected boolean isSameMemberId(Long groupMemberId) {
        return this.groupMemberId.equals(groupMemberId);
    }

    protected void leave() {
        this.isLeft = true;
    }

    protected boolean isNotLeft() {
        return !isLeft;
    }

    protected void comeBack() {
        this.isLeft = false;
    }

    protected boolean isLeftMember(GroupMember groupMember) {
        return this.groupMemberId.equals(groupMember.getGroupMemberId());
    }
}
