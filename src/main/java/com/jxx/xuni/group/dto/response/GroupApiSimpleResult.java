package com.jxx.xuni.group.dto.response;

import lombok.Getter;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_CREATED;

@Getter
public class GroupApiSimpleResult {
    private Integer status;
    private String message;

    private GroupApiSimpleResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static GroupApiSimpleResult created() {
        return new GroupApiSimpleResult(201, GROUP_CREATED);
    }

    public static GroupApiSimpleResult ok() {
        return new GroupApiSimpleResult(200, GROUP_CREATED);
    }
}
