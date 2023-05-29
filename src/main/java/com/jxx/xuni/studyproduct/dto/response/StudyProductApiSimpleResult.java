package com.jxx.xuni.studyproduct.dto.response;

import lombok.Getter;

import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_CREATED;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_DETAIL_CREATED;

@Getter
public class StudyProductApiSimpleResult {
    private Integer status;
    private String message;

    private StudyProductApiSimpleResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static StudyProductApiSimpleResult createDetail() {
        return new StudyProductApiSimpleResult(201, STUDY_PRODUCT_DETAIL_CREATED);
    }
}
