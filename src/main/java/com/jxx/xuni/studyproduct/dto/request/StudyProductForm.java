package com.jxx.xuni.studyproduct.dto.request;

import com.jxx.xuni.studyproduct.domain.Category;

public record StudyProductForm(
        String name,
        Category category,
        String content,
        String author
){}
