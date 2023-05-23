package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.StudyProductDetail;

import java.util.List;

public record StudyProductDetailReadResponse(
        String name,
        Category category,
        String content,
        String author,
        String image,
        List<StudyProductDetail> studyProductDetail
)
{ }
