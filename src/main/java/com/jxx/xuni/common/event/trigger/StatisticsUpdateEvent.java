package com.jxx.xuni.common.event.trigger;

public record StatisticsUpdateEvent(
        Long memberId,
        String studyProductId

) {}