package com.jxx.xuni.group.dto.response;

import com.jxx.xuni.group.domain.*;
import lombok.Getter;

@Getter
public class GroupReadAllResponse {
    private Long groupId;
    private Capacity capacity;
    private GroupStatus groupStatus;
    private Host host;
    private Study study;
    private Time time;
    private Period period;

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
