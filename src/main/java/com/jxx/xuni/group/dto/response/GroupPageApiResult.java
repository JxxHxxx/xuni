package com.jxx.xuni.group.dto.response;


import com.jxx.xuni.group.query.GroupAllQueryResponse;
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
}