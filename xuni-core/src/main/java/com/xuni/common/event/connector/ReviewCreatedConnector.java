package com.xuni.common.event.connector;

import com.xuni.common.event.trigger.GroupAccessedEvent;

public interface ReviewCreatedConnector {
    int receive(GroupAccessedEvent event);
}