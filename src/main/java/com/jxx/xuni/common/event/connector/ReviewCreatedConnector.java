package com.jxx.xuni.common.event.connector;

import com.jxx.xuni.common.event.trigger.ReviewCreatedEvent;

public interface ReviewCreatedConnector {
    int receive(ReviewCreatedEvent event);
}