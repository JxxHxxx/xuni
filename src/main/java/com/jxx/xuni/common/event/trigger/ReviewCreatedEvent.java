package com.jxx.xuni.common.event.trigger;

public record ReviewCreatedEvent(
        Long memberId,
        String studyProductId
) {
    public static ReviewCreatedEvent by(StatisticsUpdateEvent event) {
        return new ReviewCreatedEvent(event.memberId(), event.studyProductId());
    }
}