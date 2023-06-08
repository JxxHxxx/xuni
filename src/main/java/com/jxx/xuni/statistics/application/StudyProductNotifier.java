package com.jxx.xuni.statistics.application;

import com.jxx.xuni.common.event.trigger.StudyProductCreatedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewCreatedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewDeletedEvent;
import com.jxx.xuni.common.event.trigger.statistics.ReviewUpdatedEvent;
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

    @EventListener(ReviewCreatedEvent.class)
    public void handle(ReviewCreatedEvent event) {
        studyProductStatisticsService.reflectReviewCreate(event.rating(), event.studyProductId());
    }

    @EventListener(ReviewUpdatedEvent.class)
    public void handle(ReviewUpdatedEvent event) {
        studyProductStatisticsService.reflectReviewUpdate(event.studyProductId(), event.ratingBeforeUpdate(), event.updatedRating());
    }

    @EventListener(ReviewDeletedEvent.class)
    public void handle(ReviewDeletedEvent event) {
        studyProductStatisticsService.reflectReviewDelete(event.studyProductId(), event.rating());
    }
}