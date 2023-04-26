package com.jxx.xuni.group.dto.response;

import lombok.Getter;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;

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

    public static GroupApiSimpleResult closeRecruitment() {
        return new GroupApiSimpleResult(200, GROUP_CLOSE_RECRUITMENT);
    }

    public static GroupApiSimpleResult start() {
        return new GroupApiSimpleResult(200, GROUP_START);
    }

    public static GroupApiSimpleResult leave() {
        return new GroupApiSimpleResult(200, LEAVE_GROUP);
    }

    public static GroupApiSimpleResult check() {
        return new GroupApiSimpleResult(200, DO_CHECK);
    }
}
