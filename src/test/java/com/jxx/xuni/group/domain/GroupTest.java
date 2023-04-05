package com.jxx.xuni.group.domain;

import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.subject.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 그룹 도메인 규칙을 검증합니다.
 */

class GroupTest {

    @DisplayName("초기화 된 Group 인스터스의 " +
            "Group Status 는 GATHERING" +
            "그룹을 만든 사람(Host)는 참여자 목록에 포함됩니다.")
    @Test
    void check_initialized_group_status() {
        Group group = makeTestGroup(5);

        assertThat(group.getGroupStatus()).isEqualTo(GroupStatus.GATHERING);

        assertThat(group.getGroupMembers().stream()
                .anyMatch(groupMember -> groupMember.isSameMemberId(group.getHost().getHostId()))).isTrue();
    }

    @DisplayName("스터디 그룹의 인원은 최소 1인에서 최대 5인 까지 가능합니다. " +
            "이외의 값을 입력할 경우 CapacityOutOfBoundException 발생합니다.")
    @ParameterizedTest(name = "[{index}] capacity = {0}")
    @ValueSource(ints = {-1, 0, 6})
    void check_group_capacity(Integer capacity) {
        Group group = makeTestGroup(capacity);
        assertThatThrownBy(() -> group.checkCapacityRange()).isInstanceOf(CapacityOutOfBoundException.class);
    }

    protected static Group makeTestGroup(Integer capacity) {
        Group group = new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(capacity),
                Study.of("자바의 정석", Category.JAVA),
                new Host(1l, "재헌"));

        return group;
    }

    // 그룹 입장 규칙
    @DisplayName("이미 들어가 있는 사용자가 그룹에 참여를 시도할 경우 " +
            "GroupJoinException 예외 발생 " +
            "예외 메시지 발생 ")
    @Test
    void join_group_fail_cause_already_join() {
        //given
        Group group = makeTestGroup(5);
        GroupMember groupMember = new GroupMember(1l, "유니");

        //when - then
        assertThatThrownBy(() -> group.join(groupMember)).isInstanceOf(GroupJoinException.class)
                .hasMessage("이미 들어가 있습니다.");
    }

    @DisplayName("남은 자리가 없는 그룹에 참여를 시도할 경우 " +
            "GroupJoinException 예외 발생 " +
            "예외 메시지 발생 ")
    @Test
    void join_group_fail_cause_left_capacity_is_0() {
        Group group = makeTestGroup(1);
        GroupMember groupMember = new GroupMember(2l, "이재헌");

        assertThatThrownBy(() -> group.join(groupMember)).isInstanceOf(GroupJoinException.class)
                .hasMessage("남은 자리가 없습니다.");
    }
}