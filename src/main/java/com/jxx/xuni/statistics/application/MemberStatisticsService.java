package com.jxx.xuni.statistics.application;

import com.jxx.xuni.statistics.domain.MemberStatistics;
import com.jxx.xuni.statistics.domain.MemberStatisticsRepository;
import com.jxx.xuni.statistics.dto.response.ReviewNeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class MemberStatisticsService {

    private final MemberStatisticsRepository memberStatisticsRepository;

    public ReviewNeedResponse findReviewNeedStatistics(Long memberId, String studyProductId) {
        MemberStatistics statistics = memberStatisticsRepository.readBy(memberId, studyProductId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        return new ReviewNeedResponse(statistics.getProgress());
    }
}
