package com.xuni.api.studyproduct.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyProductApiMessage {
    public static final String STUDY_PRODUCT_CREATED = "스터디 상품 등록 완료";
    public static final String STUDY_PRODUCT_DETAIL_CREATED = "스터디 상품 상세 등록 완료";
    public static final String NOT_EXIST_STUDY_PRODUCT = "존재하지 않은 스터디 상품입니다";
    public static final String STUDY_PRODUCT_READ = "전체 조회 완료";
    public static final String STUDY_PRODUCT_DETAIL_READ = "스터디 상품 상세 조회 완료";

    public static final String SEARCH_STUDY_PRODUCT_COND = "스터디 상품 조건 조회 완료";
}
