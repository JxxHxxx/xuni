package com.jxx.xuni.common.event.connector;

import com.jxx.xuni.common.event.trigger.GroupAccessedEvent;

public interface ReviewCreatedConnector {
    int receive(GroupAccessedEvent event);
}