package com.xuni.api.studyproduct.dto.response;

import com.xuni.common.domain.Category;
import com.xuni.studyproduct.domain.Content;

import java.util.List;

public record StudyProductContentReadResponse(
        String name,
        Category category,
        String creator,
        String thumbnail,
        List<Content> contents
)
{ }
