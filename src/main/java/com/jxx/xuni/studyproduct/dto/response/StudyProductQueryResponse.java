package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.common.domain.Category;
import com.querydsl.core.annotations.QueryProjection;

public record StudyProductQueryResponse(
        String studyProductId,
        String name,
        Category category,
        String creator,
        String thumbnail
) {
    @QueryProjection
    public StudyProductQueryResponse(String studyProductId, String name, Category category, String creator, String thumbnail) {
        this.studyProductId = studyProductId;
        this.name = name;
        this.category = category;
        this.creator = creator;
        this.thumbnail = thumbnail;
    }
}
