package com.jxx.xuni.group.domain;

import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.GroupStartException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import com.jxx.xuni.group.dto.request.StudyCheckForm;
import com.jxx.xuni.studyproduct.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
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

    @DisplayName("스터디 그룹의 인원은 최소 1인에서 최대 20인 까지 가능합니다. " +
            "이외의 값을 입력할 경우 CapacityOutOfBoundException 발생합니다.")
    @ParameterizedTest(name = "[{index}] capacity = {0}")
    @ValueSource(ints = {-1, 0, 21})
    void check_group_capacity(Integer capacity) {
        Group group = makeTestGroup(capacity);
        assertThatThrownBy(() -> group.checkCapacityRange()).isInstanceOf(CapacityOutOfBoundException.class);
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
                .hasMessage(ALREADY_JOIN);
    }

    @DisplayName("남은 자리가 없는 그룹에 참여를 시도할 경우 " +
            "GroupJoinException 예외 발생 " +
            "예외 메시지 발생 ")
    @Test
    void join_group_fail_cause_left_capacity_is_0() {
        Group group = makeTestGroup(1);
        GroupMember groupMember = new GroupMember(2l, "이재헌");

        assertThatThrownBy(() -> group.join(groupMember)).isInstanceOf(GroupJoinException.class)
                .hasMessage(NOT_LEFT_CAPACITY);
    }

    @DisplayName("모집 중 상태가 아닌 그룹에 참여할 수 없습니다. " +
            "GroupJoinException 예외 발생 " +
            "예외 메시지 발생 ")
    @Test
    void join_group_fail_cause_group_status_not_gathering() {
        //given
        Group group = makeTestGroup(2);
        group.closeRecruitment(1l);

        //when - then
        GroupMember groupMember = new GroupMember(2l, "유니");
        Assertions.assertThatThrownBy(() -> group.join(groupMember))
                .isInstanceOf(GroupJoinException.class);
    }

    @DisplayName("그룹 호스트가 아닌 사람은 그룹 모집을 마감하려 할 경우 " +
            "권한 없음 예외가 발생합니다.")
    @Test
    void close_recruitment_of_group_fail_cause_is_not_host() {
        Group group = makeTestGroup(2);
        
        Assertions.assertThatThrownBy(() -> group.closeRecruitment(2l))
                .isInstanceOf(NotPermissionException.class)
                .hasMessage(NOT_PERMISSION);
    }

    @DisplayName("그룹 상태가 GATHERING 이 아닐 경우 그룹 모집을 마감할 수 없습니다. " +
            "권한 없음 예외가 발생합니다.")
    @ParameterizedTest
    @EnumSource(names = {"GATHER_COMPLETE", "START", "END"})
    void close_recruitment_of_group_fail_cause_inappropriate(GroupStatus groupStatus) {
        Group group = Group.builder()
                .capacity(new Capacity(5))
                .host(new Host(1l, "재헌"))
                .groupStatus(groupStatus)
                .build();

        Assertions.assertThatThrownBy(() -> group.closeRecruitment(1l))
                .isInstanceOf(NotAppropriateGroupStatusException.class)
                .hasMessage(NOT_APPROPRIATE_GROUP_STATUS);
    }

    @DisplayName("그룹 시작이 성공적으로 작동했다면 " +
            "GroupStatus == START, " +
            "StudyChecks 에 memberId, chapterId, title, isDone 데이터가 추가 되어야 한다." +
            "그리고 isDone 초기값은 false 이다.")
    @Test
    void start_success() {
        //given
        Group group = Group.builder()
                .capacity(new Capacity(5))
                .host(new Host(1l, "재헌"))
                .groupStatus(GATHERING)
                .build();

        //given 그룹에 멤버 추가
        group.join(new GroupMember(2l, "유니"));
        //given 모집 마감
        group.closeRecruitment(1l);

        List<StudyCheckForm> studyCheckForms = new ArrayList<>();
        studyCheckForms.add(new StudyCheckForm(1l, "객체 지향의 사실과 오해"));
        //when
        group.start(1l, studyCheckForms);
        //then - 그룹 상태는 START 로 변경된다.
        Assertions.assertThat(group.getGroupStatus()).isEqualTo(START);

        Assertions.assertThat(group.getStudyChecks().get(0).getTitle()).isEqualTo("객체 지향의 사실과 오해");
        Assertions.assertThat(group.getStudyChecks().get(0).getChapterId()).isEqualTo(1l);
        //then studyChecks 는 그룹에 참여중인 MemberId를 모두 가지고 있다.
        List<Long> members = group.getStudyChecks().stream().map(studyCheck -> studyCheck.getMemberId()).toList();
        Assertions.assertThat(members).contains(1l, 2l);
        //then studyChecks isDone 초기화 값은 false다.
        List<Boolean> isDones = group.getStudyChecks().stream().map(studyCheck -> studyCheck.isDone()).toList();
        Assertions.assertThat(isDones).containsOnly(false);

    }

    @DisplayName("그룹 호스트가 아닌 그룹 멤버가 그룹 시작을 할 경우 " +
            "NotPermissionException 예외가 발생합니다.")
    @Test
    void start_fail_cause_is_not_host() {
        Long hostId = 1l;
        Long groupMemberId = 2l;

        Group group = Group.builder()
                .capacity(new Capacity(5))
                .host(new Host(hostId, "재헌"))
                .groupStatus(GATHER_COMPLETE)
                .build();

        Assertions.assertThatThrownBy(() -> group.start(groupMemberId, null))
                .isInstanceOf(NotPermissionException.class)
                .hasMessage(NOT_PERMISSION);
    }

    @DisplayName("그룹 상태가 GATHER_COMPLETE 가 아닌 상태에서 그룹 시작을 할 경우 " +
            " NotAppropriateGroupStatusException 예외가 발생합니다.")
    @ParameterizedTest
    @EnumSource(names = {"GATHERING","START","END"})
    void start_fail_cause_is_not_gather_complete_status(GroupStatus groupStatus) {
        Group group = Group.builder()
                .capacity(new Capacity(5))
                .host(new Host(1l, "재헌"))
                .groupStatus(groupStatus)
                .build();

        Assertions.assertThatThrownBy(() -> group.start(1l, null))
                .isInstanceOf(NotAppropriateGroupStatusException.class)
                .hasMessage(NOT_APPROPRIATE_GROUP_STATUS);
    }

    @DisplayName("그룹 호스트가 아닌 그룹 멤버가 그룹 시작을 할 경우 " +
            "NotPermissionException 예외가 발생합니다.")
    @ParameterizedTest
    @NullSource
    @EmptySource
    void start_fail_cause_is_empty_or_null_study_check_form(List<StudyCheckForm> studyCheckForms) {
        Group group = Group.builder()
                .capacity(new Capacity(5))
                .host(new Host(1l, "재헌"))
                .groupStatus(GATHER_COMPLETE)
                .build();

        Assertions.assertThatThrownBy(() -> group.start(1l, studyCheckForms))
                .isInstanceOf(GroupStartException.class);

    }

    protected static Group makeTestGroup(Integer capacity) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(capacity),
                Study.of("UUID","자바의 정석", Category.JAVA),
                new Host(1l, "재헌"));
    }
}