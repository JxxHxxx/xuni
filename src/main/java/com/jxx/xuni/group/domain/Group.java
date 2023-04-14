package com.jxx.xuni.group.domain;

import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.group.domain.Capacity.*;
import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_group")
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


    public Group(Period period, Time time, Capacity capacity, Study study, Host host) {
        this.groupStatus = GATHERING;
        this.period = period;
        this.time = time;
        this.capacity = capacity;
        this.study = study;
        this.host = host;
        this.version = 0l;
        addInGroup(new GroupMember(host.getHostId(), host.getHostName()));
    }

    @Builder
    protected Group(Long id, GroupStatus groupStatus, Period period, Time time, Capacity capacity, Study study, Host host) {
        this.id = id;
        this.groupStatus = groupStatus;
        this.period = period;
        this.time = time;
        this.capacity = capacity;
        this.study = study;
        this.host = host;
        addInGroup(new GroupMember(host.getHostId(), host.getHostName()));
    }

    public void verifyCreateRule() {
        isGroupState(GATHERING);
        checkCapacityRange();
    }

    public void join(GroupMember member) {
        isAlreadyJoin(member);
        checkLeftCapacity();
        isAccessibleGroupStatus();

        addInGroup(member);
    }

    public void closeRecruitment(Long memberId) {
        isHost(memberId);
        isGroupState(GATHERING);
        this.groupStatus = GATHER_COMPLETE;
    }

    private void isHost(Long memberId) {
        if (this.host.isNotHost(memberId)) throw new NotPermissionException(NOT_PERMISSION);
    }


    protected void isGroupState(GroupStatus status) {
        if (!this.groupStatus.equals(status)){
            throw new NotAppropriateGroupStatusException(NOT_APPROPRIATE_GROUP_STATUS);
        }
    }

    protected void checkCapacityRange() {
        if (this.capacity.getTotalCapacity() > CAPACITY_MAX || this.capacity.getTotalCapacity() < CAPACITY_MIN) {
            throw new CapacityOutOfBoundException(NOT_APPROPRIATE_GROUP_CAPACITY);
        }
    }

    private void addInGroup(GroupMember groupMember) {
        groupMembers.add(groupMember);
        this.capacity.subtractOneLeftCapacity();
    }

    private void checkLeftCapacity() {
        if (capacity.isNotLeftCapacity()) throw new GroupJoinException(NOT_LEFT_CAPACITY);
    }

    private void isAlreadyJoin(GroupMember member) {
         if (groupMembers.stream().anyMatch(groupMember -> groupMember.isSameMemberId(member.getGroupMemberId())))
             throw new GroupJoinException(ALREADY_JOIN);
    }

    private void isAccessibleGroupStatus() {
        if (!GATHERING.equals(groupStatus)) throw new GroupJoinException(NOT_ACCESSIBLE_GROUP);

    }
}
