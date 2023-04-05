package com.jxx.xuni.group.dto.response;

import lombok.Getter;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_CREATED;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_JOIN;

@Getter
public class GroupApiSimpleResult {
    private Integer status;
    private String message;

    private GroupApiSimpleResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static GroupApiSimpleResult createGroup() {
        return new GroupApiSimpleResult(201, GROUP_CREATED);
    }

    public static GroupApiSimpleResult join() {
        return new GroupApiSimpleResult(200, GROUP_JOIN);
    }
}
