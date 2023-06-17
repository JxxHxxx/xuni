package com.jxx.xuni.group.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GroupPageApiResult {
    private final Integer status;
    private final String message;
    private final List<GroupAllQueryResponse> response;
    private final PageInfo pageInfo;

    public GroupPageApiResult(String message, List<GroupAllQueryResponse> response, PageInfo pageInfo) {
        this.status = 200;
        this.message = message;
        this.response = response;
        this.pageInfo = pageInfo;
    }
}