package com.xuni.common.event.trigger.statistics;

public record ReviewUpdatedEvent(
        String studyProductId,
        Integer ratingBeforeUpdate,
        Integer updatedRating
) {
}
