package com.jxx.xuni.studyproduct.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProductDetail {

    private Long chapterId;
    private String title;

    public StudyProductDetail(Long chapterId, String title) {
        this.chapterId = chapterId;
        this.title = title;
    }
}
