package com.jxx.xuni.common.event.trigger.statistics;

public record ReviewDeletedEvent(
        String studyProductId,
        Integer rating
) {}
