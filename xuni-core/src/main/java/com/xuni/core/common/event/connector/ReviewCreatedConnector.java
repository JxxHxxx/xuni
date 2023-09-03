package com.xuni.core.common.event.connector;

import com.xuni.core.common.event.trigger.GroupAccessedEvent;

public interface ReviewCreatedConnector {
    int receive(GroupAccessedEvent event);
}