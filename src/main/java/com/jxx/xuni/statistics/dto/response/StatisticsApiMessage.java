package com.jxx.xuni.statistics.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticsApiMessage {
    public static final String REVIEW_NEED_DATA = "리뷰 생성에 필요한 데이터 조회 완료";

    public static final String STUDY_PRODUCT_STAT_READ_ALL = "스터디 상품 통계 다건 조회 완료";
    public static final String STUDY_PRODUCT_STAT_READ_ONE = "스터디 상품 통계 단건 조회 완료";
}