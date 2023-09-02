package com.xuni.api.studyproduct.query;

import com.querydsl.core.annotations.QueryProjection;
import com.xuni.common.domain.Category;

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
