package com.xuni.api.group.application;

import com.xuni.api.group.dto.request.GroupCreateForm;
import com.xuni.group.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCreateService {

    private final GroupRepository groupRepository;
    private final SimpleHostCreator hostCreator;

    public Long create(Long memberId, GroupCreateForm form) {
        Host host = hostCreator.createHost(memberId);
        Group group = Group.builder()
                .name(form.name())
                .period(Period.of(form.startDate(), form.endDate()))
                .time(Time.of(form.startTime(), form.endTime()))
                .capacity(Capacity.of(form.capacity()))
                .study(Study.of(form.studyProductId(), form.subject(), form.category()))
                .host(host)
                .build();

        group.verifyCreateRule();
        Group savedGroup = groupRepository.save(group);
        return savedGroup.getId();
    }
}
