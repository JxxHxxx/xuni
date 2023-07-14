package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.group.dto.response.PageInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class StudyProductPageApiResult<T> {
    private final Integer status;
    private final String message;
    private final List<T> response;
    private final PageInfo pageInfo;

    public StudyProductPageApiResult(String message, List<T> response, PageInfo pageInfo) {
        this.status = 200;
        this.message = message;
        this.response = response;
        this.pageInfo = pageInfo;
    }
}
