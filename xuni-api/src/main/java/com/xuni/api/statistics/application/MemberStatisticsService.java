package com.xuni.api.statistics.application;

import com.xuni.api.statistics.dto.response.ReviewNeedResponse;
import com.xuni.api.statistics.infra.MemberStatisticsRepository;
import com.xuni.common.event.trigger.StatisticsAccessedEvent;
import com.xuni.statistics.domain.MemberStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class MemberStatisticsService {

    private final MemberStatisticsRepository memberStatisticsRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewNeedResponse readOne(Long memberId, String studyProductId) {
        StatisticsAccessedEvent event = new StatisticsAccessedEvent(memberId, studyProductId);
        eventPublisher.publishEvent(event);

        MemberStatistics statistics = memberStatisticsRepository.readBy(memberId, studyProductId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_ENTITY));

        return new ReviewNeedResponse(statistics.getProgress());
    }
}
