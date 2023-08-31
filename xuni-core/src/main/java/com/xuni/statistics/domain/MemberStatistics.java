package com.xuni.statistics.domain;

import com.xuni.statistics.domain.exception.ProgressOutOfBoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.xuni.statistics.domain.exception.StatisticsExceptionMessage.NOT_APPROPRIATE_PROGRESS;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStatistics {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_statistics_id")
    private Long id;
    private Long memberId;
    private String studyProductId;
    private Integer progress;

    @Builder
    public MemberStatistics(Long memberId, String studyProductId, Integer progress) {
        validParameter(progress);

        this.memberId = memberId;
        this.studyProductId = studyProductId;
        this.progress = progress;
    }

    public void updateProgress(Integer progress) {
        validParameter(progress);
        if (isNotSame(progress)) this.progress = progress;
    }

    private boolean isNotSame(Integer progress) {
        return this.progress != progress;
    }

    private static void validParameter(Integer progress) {
        if (progress > 100 || progress < 0) {
            throw new ProgressOutOfBoundException(NOT_APPROPRIATE_PROGRESS);
        }
    };
}
