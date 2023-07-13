package com.jxx.xuni.group.domain;

import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.GroupStartException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import com.jxx.xuni.group.dto.request.GroupTaskForm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.BAD_REQUEST;
import static com.jxx.xuni.group.domain.GroupMember.*;
import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_group", indexes = @Index(name = "study_group_category", columnList = "category"))
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private GroupStatus groupStatus;
    private LocalDateTime createdDate;
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public Group(Period period, Time time, Capacity capacity, Study study, Host host) {
        this.groupStatus = GATHERING;
        this.period = period;
        this.time = time;
        this.capacity = capacity;
        this.study = study;
        this.host = host;
        this.version = 0l;
        this.createdDate = LocalDateTime.now();

        this.groupMembers.add(enrollHost(host, this));
        this.capacity.subtractOneLeftCapacity();
    }

    public void verifyCreateRule() {
        checkGroupState(GATHERING);
        checkCapacityRange();
    }

    protected void checkCapacityRange() {
        if (capacity.hasNotTotalCapacityWithinRange()) {
            throw new CapacityOutOfBoundException(NOT_APPROPRIATE_GROUP_CAPACITY);
        }
    }

    protected void checkGroupState(GroupStatus status) {
        if (!groupStatus.equals(status)) throw new NotAppropriateGroupStatusException(NOT_APPROPRIATE_GROUP_STATUS);
    }

    public void join(GroupMember member) {
        checkAlreadyJoin(member);
        checkLeftCapacity();
        checkAccessibleGroupStatus();

        addInGroup(member);
    }
    // 그룹 내 존재, 탈퇴 플래그 false(그룹에 나간 상태가 아니다.) -> 이미 소속되어 있는 상태이니 예외를 던져라.
    private void checkAlreadyJoin(GroupMember member) {
        if (groupMembers.stream().anyMatch(groupMember -> (groupMember.hasEqualId(member.getGroupMemberId()) && groupMember.hasNotLeft())))
            throw new GroupJoinException(ALREADY_JOIN);
    }

    private void checkLeftCapacity() {
        if (capacity.hasNotLeftCapacity()) throw new GroupJoinException(NOT_LEFT_CAPACITY);
    }

    private void checkAccessibleGroupStatus() {
        if (!GATHERING.equals(groupStatus)) throw new GroupJoinException(NOT_ACCESSIBLE_GROUP);
    }

    public void leave(Long groupMemberId) {
        checkNotHost(groupMemberId);
        checkAbleToLeaveGroupStatus();

        GroupMember canLeaveMember = validateAbleToLeaveMember(groupMemberId);
        exceptInGroup(canLeaveMember);
    }

    private void checkNotHost(Long memberId) {
        if (host.isHost(memberId)) throw new NotPermissionException(NOT_PERMISSION);
    }

    private void checkAbleToLeaveGroupStatus() {
        if (groupStatus.equals(END)) throw new NotAppropriateGroupStatusException(NOT_APPROPRIATE_GROUP_STATUS);
    }

    private GroupMember validateAbleToLeaveMember(Long groupMemberId) {
        return groupMembers.stream()
                .filter(groupMember -> groupMember.hasEqualId(groupMemberId))
                .filter(GroupMember::hasNotLeft)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTED_GROUP_MEMBER));
    }

    public void closeRecruitment(Long memberId) {
        checkHost(memberId);
        checkGroupState(GATHERING);

        changeGroupStatusTo(GATHER_COMPLETE);
    }

    protected void changeGroupStatusTo(GroupStatus groupStatus) {
        this.groupStatus = groupStatus;
    }

    // 호스트인지 검증 - 호스트라면 통과 아니면 예외
    private void checkHost(Long memberId) {
        if (host.isNotHost(memberId)) throw new NotPermissionException(NOT_PERMISSION);
    }

    public void start(Long memberId, List<GroupTaskForm> groupTaskForms) {
        checkHost(memberId);
        checkGroupState(GATHER_COMPLETE);
        checkEmptyOrNullGroupTaskForm(groupTaskForms);
        initializeGroupTask(groupTaskForms);

        changeGroupStatusTo(START);
    }

    private void checkEmptyOrNullGroupTaskForm(List<GroupTaskForm> groupTaskForms) {
        if (groupTaskForms == null || groupTaskForms.isEmpty()) throw new GroupStartException(CURRICULUM_REQUIRED);
    }

    protected void initializeGroupTask(List<GroupTaskForm> groupTaskForms) {
        List<GroupMember> notLeftGroupMembers = groupMembers.stream().filter(groupMember -> groupMember.hasNotLeft()).toList();

        notLeftGroupMembers.stream()
                .map(groupMember -> prepareTasks(groupTaskForms, groupMember))
                .forEach(initializedTasks -> tasks.addAll(initializedTasks));
    }

    public void doTask(Long chapterId, Long groupMemberId) {
        checkGroupState(START);
        Task task = validateTaskManagingAuthority(chapterId, groupMemberId);
        task.updateDone();
    }

    private Task validateTaskManagingAuthority(Long chapterId, Long groupMemberId) {
        return tasks.stream()
                .filter(task -> task.isSameChapter(chapterId))
                .filter(task -> task.isEqualMemberId(groupMemberId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(BAD_REQUEST));
    }

    public void updateGroupMemberLastVisitedTime(Long memberId) {
        Optional<GroupMember> requestMember = getRequestMember(memberId);
        if (isGroupMember(requestMember)) {
            requestMember.get().updateLastVisitedTime();
        }
    }

    private Optional<GroupMember> getRequestMember(Long memberId) {
        return groupMembers.stream()
                .filter(groupMember -> groupMember.hasEqualId(memberId))
                .filter(GroupMember::hasNotLeft)
                .findFirst();
    }
    
    private boolean isGroupMember(Optional<GroupMember> requestMember) {
        return requestMember.isPresent();
    }

    public List<Task> receiveTasksOf(Long memberId) {
        return tasks.stream()
                .filter(task -> task.isEqualMemberId(memberId))
                .toList();
    }

    public int receiveProgress(Long memberId) {
        List<Task> memberTasks = receiveTasksOf(memberId);
        int totalTaskAmount = memberTasks.size();
        
        long doneTaskAmount = memberTasks.stream()
                .filter(Task::isDone)
                .count();

        return calculateProgress(totalTaskAmount, (double) doneTaskAmount);
    }

    private int calculateProgress(int taskAmount, double doneTaskAmount) {
        double middleResult = doneTaskAmount / taskAmount;
        return (int) (middleResult * 100);
    }

    private void exceptInGroup(GroupMember groupMember) {
        groupMember.leave();
        capacity.addOneLeftCapacity();
    }

    private List<Task> prepareTasks(List<GroupTaskForm> groupTaskForms, GroupMember groupMember) {
        return groupTaskForms.stream()
                .map(groupTaskForm -> Task.initialize(
                        groupMember.getGroupMemberId(),
                        groupTaskForm.chapterId(),
                        groupTaskForm.title(),
                        this))
                .toList();
    }

    private void addInGroup(GroupMember member) {
        Optional<GroupMember> optionalGroupMember = groupMembers.stream()
                .filter(groupMember -> groupMember.isLeftMember(member))
                .findFirst();

        if (optionalGroupMember.isPresent()) {
            optionalGroupMember.get().comeBack();
        }
        if (optionalGroupMember.isEmpty()) {
            groupMembers.add(member);
        }

        capacity.subtractOneLeftCapacity();
    }
}
