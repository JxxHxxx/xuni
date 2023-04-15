package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.studyproduct.domain.StudyProduct;
import com.jxx.xuni.studyproduct.domain.StudyProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.NOT_EXISTED_GROUP;

@Service
@RequiredArgsConstructor
public class GroupManagingService {

    private final GroupRepository groupRepository;
    private final StudyProductRepository studyProductRepository;

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
    public void start(MemberDetails memberDetails, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP));

        StudyProduct studyProduct = studyProductRepository.findById(group.getStudy().getId()).orElseThrow();
        List<StudyCheck> studyChecks = group.getStudyChecks();

        int size = group.getGroupMembers().size();
        for (int i = 0; i < size; i++) {
            studyChecks.addAll(createStudyCheck(i, group, studyProduct));
        }

        group.start(memberDetails.getUserId());
    }

    private List<StudyCheck> createStudyCheck(int idx, Group group, StudyProduct studyProduct) {
        return studyProduct.getStudyProductDetail().stream().map(studyProductDetail -> StudyCheck.init(
                group.getGroupMembers().get(idx).getGroupMemberId(),
                studyProductDetail.getChapterId(),
                studyProductDetail.getTitle())).toList();
    }
}
