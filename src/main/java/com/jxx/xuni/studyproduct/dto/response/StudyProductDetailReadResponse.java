package com.jxx.xuni.studyproduct.dto.response;

import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.studyproduct.domain.StudyProductDetail;
import com.jxx.xuni.studyproduct.domain.Topic;

import java.util.List;

public record StudyProductDetailReadResponse(
        String name,
        Category category,
        Topic topic,
        List<StudyProductDetail> studyProductDetail
)
{ }
