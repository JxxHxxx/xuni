package com.jxx.xuni.studyproduct.query;

import com.jxx.xuni.studyproduct.dto.response.StudyProductQueryResponse;
import com.jxx.xuni.studyproduct.query.dynamic.StudyProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyProductQuery {
    Page<StudyProductQueryResponse> searchStudyProduct(StudyProductSearchCondition condition, Pageable pageable);
}
