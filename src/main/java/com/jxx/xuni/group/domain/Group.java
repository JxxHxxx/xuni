package com.jxx.xuni.group.domain;

import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.GroupStartException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import com.jxx.xuni.group.dto.request.StudyCheckForm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.BAD_REQUEST;
import static com.jxx.xuni.group.domain.Capacity.*;
import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_group", indexes = @Index(name = "study_group_category", columnList = "category"))
public class Group {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private GroupStatus groupStatus;
    @Embedded
    private Period period;
    @Embedded
    private Time time;
    @Embedded
    private Capacity capacity;
    @Embedded
    private Study study;
    @Embedded
    private Host host;
    @Version
    private long version;

    @ElementCollection
    @CollectionTable(name = "group_member", joinColumns = @JoinColumn(name = "group_id"))
    private List<GroupMember> groupMembers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "group_study_check", joinColumns = @JoinColumn(name = "group_id"))
    private List<StudyCheck> studyChecks = new ArrayList<>();

    public Group(Period period, Time time, Capacity capacity, Study study, Host host) {
        this.groupStatus = GATHERING;
        this.period = period;
        this.time = time;
        this.capacity = capacity;
        this.study = study;
        this.host = host;
        this.version = 0l;

        this.groupMembers.add(new GroupMember(host.getHostId(), host.getHostName()));
        this.capacity.subtractOneLeftCapacity();

    }

    @Builder
    protected Group(Long id, GroupStatus groupStatus, Period period, Time time, Capacity capacity, Study study, Host host, List<GroupMember> groupMembers) {
        this.id = id;
        this.groupStatus = groupStatus;
        this.period = period;
        this.time = time;
        this.capacity = capacity;
        this.study = study;
        this.host = host;
        this.groupMembers = groupMembers;

        addInGroup(new GroupMember(host.getHostId(), host.getHostName()));
    }

    public void verifyCreateRule() {
        checkGroupState(GATHERING);
        checkCapacityRange();
    }

    public void join(GroupMember member) {
        checkAlreadyJoin(member);
        checkLeftCapacity();
        checkAccessibleGroupStatus();

        addInGroup(member);
    }

    public void leave(Long groupMemberId) {
        checkNotHost(groupMemberId);
        checkAbleToLeaveGroupStatus();

        GroupMember leavableMember = validateAbleToLeaveMember(groupMemberId);
        exceptInGroup(leavableMember);
    }

    public void closeRecruitment(Long memberId) {
        checkHost(memberId);
        checkGroupState(GATHERING);

        changeGroupStatusTo(GATHER_COMPLETE);
    }

    public void start(Long memberId, List<StudyCheckForm> studyCheckForms) {
        checkHost(memberId);
        checkGroupState(GATHER_COMPLETE);
        checkNullAndEmptyStudyCheckForm(studyCheckForms);
        initStudyChecks(studyCheckForms);

        changeGroupStatusTo(START);
    }

    public void verifyCheckRule(Long chapterId, Long groupMemberId) {
        checkGroupState(START);
        StudyCheck studyCheck = validateAbleToCheckStudyCheck(chapterId, groupMemberId);
        studyCheck.check();
    }

    private StudyCheck validateAbleToCheckStudyCheck(Long chapterId, Long groupMemberId) {
        return studyChecks.stream()
                .filter(s -> s.isSameChapter(chapterId))
                .filter(s -> s.isSameMember(groupMemberId))
                .findAny().orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST));
    }

    private GroupMember validateAbleToLeaveMember(Long groupMemberId) {
        return groupMembers.stream()
                .filter(g -> g.getGroupMemberId().equals(groupMemberId))
                .filter(g -> g.hasNotLeft())
                .findAny().orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP_MEMBER));
    }

    private void exceptInGroup(GroupMember groupMember) {
        groupMember.leave();
        capacity.addOneLeftCapacity();
    }

    private void checkAbleToLeaveGroupStatus() {
        if (groupStatus.equals(END)) throw new NotAppropriateGroupStatusException(NOT_APPROPRIATE_GROUP_STATUS);
    }

    private void checkNotHost(Long memberId) {
        if (host.isHost(memberId)) throw new NotPermissionException(NOT_PERMISSION);
    }

    // 호스트인지 검증 - 호스트라면 통과 아니면 예외
    private void checkHost(Long memberId) {
        if (host.isNotHost(memberId)) throw new NotPermissionException(NOT_PERMISSION);
    }

    protected void initStudyChecks(List<StudyCheckForm> studyCheckForms) {
        List<GroupMember> realGroupMember = groupMembers.stream().filter(groupMember -> groupMember.hasNotLeft()).toList();
        for (GroupMember groupMember : realGroupMember) {
            List<StudyCheck> studyChecks = studyCheckForms.stream()
                    .map(s -> StudyCheck.init(groupMember.getGroupMemberId(), s.chapterId(), s.title()))
                    .toList();

            this.studyChecks.addAll(studyChecks);
        }
    }

    private void checkNullAndEmptyStudyCheckForm(List<StudyCheckForm> studyCheckForms) {
        if (studyCheckForms == null || studyCheckForms.isEmpty()) throw new GroupStartException("커리큘럼은 필수입니다.");
    }

    protected void checkGroupState(GroupStatus status) {
        if (!groupStatus.equals(status)) throw new NotAppropriateGroupStatusException(NOT_APPROPRIATE_GROUP_STATUS);

    }

    protected void checkCapacityRange() {
        if (capacity.getTotalCapacity() > CAPACITY_MAX || capacity.getTotalCapacity() < CAPACITY_MIN) {
            throw new CapacityOutOfBoundException(NOT_APPROPRIATE_GROUP_CAPACITY);
        }
    }

    private void addInGroup(GroupMember member) {
        Optional<GroupMember> optionalGroupMember = groupMembers.stream().filter(g -> g.isLeftMember(member)).findFirst();
        if (optionalGroupMember.isPresent()) {
            optionalGroupMember.get().comeBack();
        }
        if (optionalGroupMember.isEmpty()) {
            groupMembers.add(member);
        }

        this.capacity.subtractOneLeftCapacity();
    }

    private void checkLeftCapacity() {
        if (capacity.hasNotLeftCapacity()) throw new GroupJoinException(NOT_LEFT_CAPACITY);
    }

    // 그룹 내 존재, 탈퇴 플래그 false(그룹에 나간 상태가 아니다.) -> 이미 소속되어 있는 상태이니 예외를 던져라.
    private void checkAlreadyJoin(GroupMember member) {
        if (groupMembers.stream().anyMatch(groupMember -> (groupMember.isSameMemberId(member.getGroupMemberId()) && groupMember.hasNotLeft())))
            throw new GroupJoinException(ALREADY_JOIN);
    }

    private void checkAccessibleGroupStatus() {
        if (!GATHERING.equals(groupStatus)) throw new GroupJoinException(NOT_ACCESSIBLE_GROUP);
    }

    protected void changeGroupStatusTo(GroupStatus groupStatus) {
        this.groupStatus = groupStatus;
    }
}
