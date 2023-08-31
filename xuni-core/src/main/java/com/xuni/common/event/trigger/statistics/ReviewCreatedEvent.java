package com.xuni.common.event.trigger.statistics;

public record ReviewCreatedEvent(
        String studyProductId,
        Integer rating
) {}
