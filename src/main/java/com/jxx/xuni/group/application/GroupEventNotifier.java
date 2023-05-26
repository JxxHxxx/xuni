package com.jxx.xuni.group.application;

import com.jxx.xuni.common.event.trigger.ReviewCreatedEvent;
import com.jxx.xuni.common.event.connector.ReviewCreatedConnector;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.query.GroupReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.NOT_EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class GroupEventNotifier implements ReviewCreatedConnector {

    private final GroupReadRepository groupReadRepository;

    @Override
    @EventListener(ReviewCreatedEvent.class)
    public int receive(ReviewCreatedEvent event) {
        Group group = groupReadRepository.findBy(event.memberId(), event.studyProductId()).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_ENTITY));
        return group.calculateProgress(event.memberId());
    }
}