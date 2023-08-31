package com.xuni.studyproduct.query.dynamic;

import com.xuni.common.domain.Category;

public record StudyProductSearchCondition(
        String name,
        String creator,
        Category category
) {

}
