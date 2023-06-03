package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.event.trigger.StudyProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyProductNotifier {

    private final StudyProductStatisticsService studyProductStatisticsService;

    @EventListener(StudyProductCreatedEvent.class)
    public void handle(StudyProductCreatedEvent event) {
        studyProductStatisticsService.create(event.studyProductId());
    }
}