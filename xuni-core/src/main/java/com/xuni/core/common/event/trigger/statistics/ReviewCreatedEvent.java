package com.xuni.core.common.event.trigger.statistics;

public record ReviewCreatedEvent(
        String studyProductId,
        Integer rating
) {}
