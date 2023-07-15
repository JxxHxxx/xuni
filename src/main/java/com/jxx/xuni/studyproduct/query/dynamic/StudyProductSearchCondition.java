package com.jxx.xuni.studyproduct.query.dynamic;

import com.jxx.xuni.common.domain.Category;

public record StudyProductSearchCondition(
        String name,
        String creator,
        Category category
) {

}
