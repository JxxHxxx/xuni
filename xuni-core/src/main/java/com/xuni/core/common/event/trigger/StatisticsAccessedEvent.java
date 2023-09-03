package com.xuni.core.common.event.trigger;

public record StatisticsAccessedEvent(
        Long memberId,
        String studyProductId

) {}