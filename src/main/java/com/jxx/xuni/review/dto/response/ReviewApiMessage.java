package com.jxx.xuni.review.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewApiMessage {

    public static final String REVIEW_CREATE = "리뷰 작성 완료";
    public static final String REVIEW_UPDATE = "리뷰 수정 완료";
    public static final String REVIEW_DELETE = "리뷰 삭제 완료";
    public static final String REVIEW_READ = "리뷰 조회 완료";
}