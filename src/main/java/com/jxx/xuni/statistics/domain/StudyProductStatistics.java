package com.jxx.xuni.statistics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// TODO : 상품이 등록될 때 만들어져야함 Event

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyProductStatistics {

    @Id @Column(name = "study_prodcut_id")
    private String id;
    private Integer ratingAvg;
    private Integer reviewCnt;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;

    @Builder
    public StudyProductStatistics(String id, Integer ratingAvg, Integer reviewCnt) {
        this.id = id;
        this.ratingAvg = ratingAvg;
        this.reviewCnt = reviewCnt;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
    }
}
