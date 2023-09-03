package com.xuni.api.studyproduct.dto.response;

import com.xuni.core.common.domain.Category;
import com.xuni.core.studyproduct.domain.Content;

import java.util.List;

public record StudyProductContentReadResponse(
        String name,
        Category category,
        String creator,
        String thumbnail,
        List<Content> contents
)
{ }
