package com.jxx.xuni.group.dto.response;

import com.jxx.xuni.group.domain.*;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupReadAllResponse {
    private Long groupId;
    private Capacity capacity;
    private GroupStatus groupStatus;
    private Host host;
    private Time time;
    private Period period;
    private List<GroupMember> groupMembers;

    public GroupReadAllResponse(Long groupId, Capacity capacity, GroupStatus groupStatus, Host host, Time time, Period period, List<GroupMember> groupMembers) {
        this.groupId = groupId;
        this.capacity = capacity;
        this.groupStatus = groupStatus;
        this.host = host;
        this.time = time;
        this.period = period;
        this.groupMembers = groupMembers;
    }
}
