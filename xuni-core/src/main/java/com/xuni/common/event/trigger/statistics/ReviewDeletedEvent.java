package com.xuni.common.event.trigger.statistics;

public record ReviewDeletedEvent(
        String studyProductId,
        Integer rating
) {}
