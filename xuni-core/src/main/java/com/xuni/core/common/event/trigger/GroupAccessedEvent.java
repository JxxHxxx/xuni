package com.xuni.core.common.event.trigger;

public record GroupAccessedEvent(
        Long memberId,
        String studyProductId
) {
    public static GroupAccessedEvent by(StatisticsAccessedEvent event) {
        return new GroupAccessedEvent(event.memberId(), event.studyProductId());
    }
}