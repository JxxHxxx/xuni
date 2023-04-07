package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.studyproduct.domain.Category;

public record StudyProductReadResponse(
        String name,
        Category category,
        String content,
        String author,
        String image
) {}