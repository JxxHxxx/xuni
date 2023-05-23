package com.jxx.xuni.group.dto.response;


import com.jxx.xuni.group.domain.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupPageApiResult {
    private Integer status;
    private String message;
    private List<GroupAllQueryResponse> response;
    private PageInfo pageInfo;

    public GroupPageApiResult(String message, List<GroupAllQueryResponse> response, PageInfo pageInfo) {
        this.status = 200;
        this.message = message;
        this.response = response;
        this.pageInfo = pageInfo;
    }

    @Getter
    public static class GroupAllQueryResponse {
        private Long groupId;
        private Capacity capacity;
        private GroupStatus groupStatus;
        private Host host;
        private Study study;
        private Time time;
        private Period period;

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
}