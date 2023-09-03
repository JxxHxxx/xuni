package com.xuni.api.statistics.application;

import com.xuni.api.statistics.infra.MemberStatisticsRepository;
import com.xuni.core.common.event.connector.ReviewCreatedConnector;
import com.xuni.core.common.event.trigger.GroupAccessedEvent;
import com.xuni.core.common.event.trigger.StatisticsAccessedEvent;
import com.xuni.core.statistics.domain.MemberStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberStatisticsNotifier {

    private final MemberStatisticsRepository memberStatisticsRepository;
    private final ReviewCreatedConnector reviewCreatedConnector;

    @EventListener(StatisticsAccessedEvent.class)
    public void handle(StatisticsAccessedEvent event) {
        int progress = reviewCreatedConnector.receive(GroupAccessedEvent.by(event));
        Optional<MemberStatistics> optionalMemberStatistics = memberStatisticsRepository.readBy(event.memberId(), event.studyProductId());

        if (optionalMemberStatistics.isEmpty()) {
            MemberStatistics statistics = new MemberStatistics(event.memberId(), event.studyProductId(), progress);
            memberStatisticsRepository.save(statistics);
        }

        if (optionalMemberStatistics.isPresent()) {
            MemberStatistics memberStatistics = optionalMemberStatistics.get();
            memberStatistics.updateProgress(progress);
        }
    }
}