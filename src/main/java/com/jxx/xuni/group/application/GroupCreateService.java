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
        Group group = new Group(Period.of(form.getStartDate(), form.getEndDate()),
                                Time.of(form.getStartTime(), form.getEndTime()),
                                new Capacity(form.getCapacity()),
                                Study.of(form.getSubject(), form.getCategory()),
                                host);

        group.verifyCreateRule();
        groupRepository.save(group);
    }
}
