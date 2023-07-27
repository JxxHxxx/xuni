package com.jxx.xuni.common.http;

import com.jxx.xuni.common.query.PageInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponseBody<T, PAGE extends PageInfo> {
    private final Integer status;
    private final String message;
    private final List<T> response;
    private final PAGE pageInfo;

    public PageResponseBody(String message, List<T> response, PAGE pageInfo) {
        this.status = 200;
        this.message = message;
        this.response = response;
        this.pageInfo = pageInfo;
    }
}
