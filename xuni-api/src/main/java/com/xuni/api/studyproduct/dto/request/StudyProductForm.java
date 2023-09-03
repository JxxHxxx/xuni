package com.xuni.api.studyproduct.dto.request;

import com.xuni.core.common.domain.Category;

public record StudyProductForm(
        String name,
        Category category,
        String creator
){}
