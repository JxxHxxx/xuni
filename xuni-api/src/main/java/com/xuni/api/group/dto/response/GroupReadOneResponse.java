package com.xuni.api.group.dto.response;

import com.xuni.group.domain.*;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupReadOneResponse {
    private final Long groupId;
    private final Capacity

            capacity;
    private final GroupStatus groupStatus;
    private final Host host;
    private final Study study;
    private final Time time;
    private final Period period;
    private final List<GroupMemberDto> groupMembers;

    public GroupReadOneResponse(Long groupId, Capacity capacity, GroupStatus groupStatus, Host host, Study study, Time time, Period period, List<GroupMemberDto> groupMembers) {
        this.groupId = groupId;
        this.capacity = capacity;
        this.groupStatus = groupStatus;
        this.host = host;
        this.study = study;
        this.time = time;
        this.period = period;
        this.groupMembers = groupMembers;
    }
}
