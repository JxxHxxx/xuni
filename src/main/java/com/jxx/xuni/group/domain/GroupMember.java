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

    public GroupMember(Long groupMemberId, String groupMemberName) {
        this.groupMemberId = groupMemberId;
        this.groupMemberName = groupMemberName;
    }

    protected boolean isSameMemberId(Long groupMemberId) {
        return this.groupMemberId.equals(groupMemberId);
    }
}
