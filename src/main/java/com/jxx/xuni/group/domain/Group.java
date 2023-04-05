package com.jxx.xuni.group.domain;

import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.group.domain.Capacity.*;
import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_UNCREATED;
import static javax.persistence.GenerationType.*;

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

    protected void isGroupState(GroupStatus status) {
        if (!this.groupStatus.equals(status)){
            throw new NotAppropriateGroupStatusException(GROUP_UNCREATED);
        }
    }

    protected void checkCapacityRange() {
        if (this.capacity.getTotalCapacity() > CAPACITY_MAX || this.capacity.getTotalCapacity() < CAPACITY_MIN) {
            throw new CapacityOutOfBoundException(GROUP_UNCREATED);
        }
    }

    private void addInGroup(GroupMember groupMember) {
        groupMembers.add(groupMember);
        this.capacity.subtractOneLeftCapacity();
    }

    private void checkLeftCapacity() {
        if (capacity.isNotLeftCapacity()) throw new GroupJoinException("남은 자리가 없습니다.");
    }

    private void isAlreadyJoin(GroupMember member) {
         if (groupMembers.stream().anyMatch(groupMember -> groupMember.isSameMemberId(member.getGroupMemberId())))
             throw new GroupJoinException("이미 들어가 있습니다.");
    }

    private void isAccessibleGroupStatus() {
        if (!GATHERING.equals(groupStatus)) throw new GroupJoinException("입장 가능한 그룹이 아닙니다.");

    }
}
