package com.jxx.xuni.group.domain;

import com.jxx.xuni.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;

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
