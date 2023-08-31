package com.xuni.api.group.dto.response;

import lombok.Getter;

@Getter
public class GroupApiReadResult<T> {
    private final Integer status;
    private final String message;
    private final T response;

    public GroupApiReadResult(String message, T response) {
        this.status = 200;
        this.message = message;
        this.response = response;
    }
}
