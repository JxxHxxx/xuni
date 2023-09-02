package com.xuni.api.studyproduct.query;

import com.xuni.api.studyproduct.query.dynamic.StudyProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyProductQuery {
    Page<StudyProductQueryResponse> searchStudyProduct(StudyProductSearchCondition condition, Pageable pageable);
}
