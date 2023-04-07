package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupMember;
import com.jxx.xuni.group.domain.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupManagingService {

    private final GroupRepository groupRepository;

    @Transactional
    public void join(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
        GroupMember groupMember = new GroupMember(memberDetails.getUserId(), memberDetails.getName());

        group.join(groupMember);
    }

    @Transactional
    public void closeRecruitment(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        group.closeRecruitment(memberDetails.getUserId());
    }
}
