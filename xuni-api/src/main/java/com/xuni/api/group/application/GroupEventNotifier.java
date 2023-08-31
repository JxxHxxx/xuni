package com.xuni.api.group.application;

import com.xuni.common.event.trigger.GroupAccessedEvent;
import com.xuni.common.event.connector.ReviewCreatedConnector;
import com.xuni.common.exception.CommonExceptionMessage;
import com.xuni.group.domain.Group;
import com.xuni.group.query.GroupReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupEventNotifier implements ReviewCreatedConnector {

    private final GroupReadRepository groupReadRepository;

    @Override
    @EventListener(GroupAccessedEvent.class)
    public int receive(GroupAccessedEvent event) {
        Group group = groupReadRepository.findBy(event.memberId(), event.studyProductId()).orElseThrow(
                () -> new IllegalArgumentException(CommonExceptionMessage.NOT_EXIST_ENTITY));
        return group.receiveProgress(event.memberId());
    }
}