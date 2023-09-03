package com.xuni.api.studyproduct.dto.response;

import com.xuni.core.common.domain.Category;

public record StudyProductReadResponse(
        String studyProductId,
        String name,
        Category category,
        String creator,
        String thumbnail
) {}