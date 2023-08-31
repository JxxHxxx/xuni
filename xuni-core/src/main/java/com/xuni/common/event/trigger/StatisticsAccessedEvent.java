package com.xuni.common.event.trigger;

public record StatisticsAccessedEvent(
        Long memberId,
        String studyProductId

) {}