package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCreateService {

    private final GroupRepository groupRepository;
    private final SimpleHostCreator hostCreator;

    public void create(Long memberId, GroupCreateForm form) {
        Host host = hostCreator.createHost(memberId);
        Group group = Group.builder()
                .period(Period.of(form.startDate(), form.endDate()))
                .time(Time.of(form.startTime(), form.endTime()))
                .capacity(Capacity.of(form.capacity()))
                .study(Study.of(form.studyProductId(), form.subject(), form.category()))
                .host(host)
                .build();

        group.verifyCreateRule();
        groupRepository.save(group);
    }
}
