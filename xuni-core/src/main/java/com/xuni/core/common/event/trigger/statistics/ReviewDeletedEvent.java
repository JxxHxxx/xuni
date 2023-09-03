package com.xuni.core.common.event.trigger.statistics;

public record ReviewDeletedEvent(
        String studyProductId,
        Integer rating
) {}
