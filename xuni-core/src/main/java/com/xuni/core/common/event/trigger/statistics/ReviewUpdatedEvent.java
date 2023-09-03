package com.xuni.core.common.event.trigger.statistics;

public record ReviewUpdatedEvent(
        String studyProductId,
        Integer ratingBeforeUpdate,
        Integer updatedRating
) {
}
