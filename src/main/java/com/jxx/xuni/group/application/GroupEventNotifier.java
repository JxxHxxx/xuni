package com.jxx.xuni.group.application;

import com.jxx.xuni.common.event.trigger.ReviewCreatedEvent;
import com.jxx.xuni.common.event.connector.ReviewCreatedConnector;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.query.GroupReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupEventNotifier implements ReviewCreatedConnector {

    private final GroupReadRepository groupReadRepository;

    @Override
    @EventListener
    public int receive(ReviewCreatedEvent event) {
        Group group = groupReadRepository.findBy(event.memberId(), event.studyProductId()).get();
        return group.calculateProgress(event.memberId());
    }
}