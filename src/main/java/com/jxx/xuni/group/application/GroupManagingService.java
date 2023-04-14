package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupMember;
import com.jxx.xuni.group.domain.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.NOT_EXISTED_GROUP;

@Service
@RequiredArgsConstructor
public class GroupManagingService {

    private final GroupRepository groupRepository;

    @Transactional
    public void join(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.readWithOptimisticLock(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));
        GroupMember groupMember = new GroupMember(memberDetails.getUserId(), memberDetails.getName());

        group.join(groupMember);
    }

    @Transactional
    public void closeRecruitment(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));

        group.closeRecruitment(memberDetails.getUserId());
    }
}
