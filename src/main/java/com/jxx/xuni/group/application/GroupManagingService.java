package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.group.dto.request.StudyCheckForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.BAD_REQUEST;
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

    @Transactional
    public void start(Long groupId, MemberDetails memberDetails, List<StudyCheckForm> studyCheckForms) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));

        group.start(memberDetails.getUserId(), studyCheckForms);

    }

    @Transactional
    public void leave(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));

        group.leave(memberDetails.getUserId());
    }

    @Transactional
    public void checkStudyChapter(MemberDetails memberDetails, Long groupId, Long chapterId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));

        List<StudyCheck> groupStudyChecks = group.getStudyChecks();
        StudyCheck studyCheck = groupStudyChecks.stream().findAny()
                .filter(s -> s.isSameChapter(chapterId))
                .filter(s -> s.isSameMember(memberDetails.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST));

        studyCheck.check();
    }
}
