package com.jxx.xuni.group.dto.response;

import com.jxx.xuni.group.domain.*;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupReadOneResponse {
    private Long groupId;
    private Capacity capacity;
    private GroupStatus groupStatus;
    private Host host;
    private Study study;
    private Time time;
    private Period period;
    private List<GroupMember> groupMembers;

    public GroupReadOneResponse(Long groupId, Capacity capacity, GroupStatus groupStatus, Host host, Study study, Time time, Period period, List<GroupMember> groupMembers) {
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
