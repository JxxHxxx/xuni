package com.jxx.xuni.group.dto.response;

import com.jxx.xuni.group.domain.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GroupAllQueryResponse {
    private final Long groupId;
    private final Capacity capacity;
    private final GroupStatus groupStatus;
    private final Host host;
    private final Study study;
    private final Time time;
    private final Period period;

    @QueryProjection
    public GroupAllQueryResponse(Long groupId, Capacity capacity, GroupStatus groupStatus, Host host, Study study, Time time, Period period) {
        this.groupId = groupId;
        this.capacity = capacity;
        this.groupStatus = groupStatus;
        this.host = host;
        this.study = study;
        this.time = time;
        this.period = period;
    }
}
