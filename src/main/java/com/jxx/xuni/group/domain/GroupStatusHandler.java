package com.jxx.xuni.group.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupStatusHandler {

    private final GroupRepository groupRepository;

    @Transactional
    public void changeToStart() {
        groupRepository.updateGroupStatusToStart();
    }

    @Transactional
    public void changeToEnd() {
        groupRepository.updateGroupStatusToEnd();
    }
}
