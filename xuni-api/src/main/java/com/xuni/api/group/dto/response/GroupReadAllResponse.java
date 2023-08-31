package com.xuni.api.group.dto.response;

import com.xuni.group.domain.*;
import lombok.Getter;

@Getter
public class GroupReadAllResponse {
    private final Long groupId;
    private final Capacity capacity;
    private final GroupStatus groupStatus;
    private final Host host;
    private final Study study;
    private final Time time;
    private final Period period;

    public GroupReadAllResponse(Long groupId, Capacity capacity, GroupStatus groupStatus, Host host, Study study, Time time, Period period) {
        this.groupId = groupId;
        this.capacity = capacity;
        this.groupStatus = groupStatus;
        this.host = host;
        this.study = study;
        this.time = time;
        this.period = period;
    }
}
