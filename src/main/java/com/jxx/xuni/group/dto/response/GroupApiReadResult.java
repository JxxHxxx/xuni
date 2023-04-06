package com.jxx.xuni.group.dto.response;

import lombok.Getter;

@Getter
public class GroupApiReadResult<T> {
    private Integer status;
    private String message;
    private T response;

    public GroupApiReadResult(String message, T response) {
        this.status = 200;
        this.message = message;
        this.response = response;
    }
}
