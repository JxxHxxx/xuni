package com.xuni.api.studyproduct.query.dynamic;

import com.xuni.core.common.domain.Category;

public record StudyProductSearchCondition(
        String name,
        String creator,
        Category category
) {

}
