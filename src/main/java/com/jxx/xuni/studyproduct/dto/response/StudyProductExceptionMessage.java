package com.jxx.xuni.studyproduct.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyProductExceptionMessage {
    public static final String NOT_EXIST_CATEGORY = "해당 카테고리에 존재하는 상품이 없습니다. 정확한 카테고리를 입력해주세요. 카테고리는 대문자 형식입니다.";
}
