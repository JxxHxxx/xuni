package com.jxx.xuni.group.dto.response;

import lombok.Getter;
import static com.jxx.xuni.group.dto.response.GroupCreateMessage.SUCCESS_MESSAGE;

@Getter
public class GroupCreateResult {
    private Integer status;
    private String message;

    private GroupCreateResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static GroupCreateResult created() {
        return new GroupCreateResult(201, SUCCESS_MESSAGE);
    }
}
