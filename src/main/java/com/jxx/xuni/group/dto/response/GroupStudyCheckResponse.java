package com.jxx.xuni.group.dto.response;

import lombok.Getter;

@Getter
public class GroupStudyCheckResponse {
    private final Long chapterId;
    private final String title;
    private final boolean isDone;

    public GroupStudyCheckResponse(Long chapterId, String title, boolean isDone) {
        this.chapterId = chapterId;
        this.title = title;
        this.isDone = isDone;
    }
}