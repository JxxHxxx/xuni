package com.jxx.xuni.group.domain;

import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.domain.MemberRepository;
import com.jxx.xuni.studyproduct.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.jxx.xuni.group.domain.GroupStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberRepository memberRepository;
    Long hostId;

    @Autowired
    PlatformTransactionManager transactionManager;

    @BeforeEach
    void beforeEach() {
        Member member = new Member(LoginInfo.of("leesin5498@naver.com", "1234"), "이재헌");
        Member savedMember = memberRepository.save(member);
        hostId = savedMember.getId();
    }
    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("그룹 엔티티 저장 및 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        Group group = TestGroupServiceSupporter.receiveSampleGroup(Long.MAX_VALUE);
        //when
        Group saveGroup = groupRepository.save(group);

        //then
        assertThat(groupRepository.findById(saveGroup.getId())).isNotEmpty();
    }

    @DisplayName("updateGroupStatusToStart() 메서드는 그룹 상태(GroupStatus)를 START로 변경한다. " +
            "조건" +
            "1. 그룹 시작일이 당일(메서드 실행 시점 당시)" +
            "2. 그룹 상태는 (GATHER_COMPLETE, GATHERING)여야 한다. " +
            "해당 단위 테스트에서는 2번 조건을 테스트한다.")
    @ParameterizedTest(name = "[{index}] 현재 그룹 상태 : {0} | 메서드 실행 후 그룹 상태 : {1}")
    @CsvSource(value = {"GATHERING, START", "GATHER_COMPLETE, START", "END, END"})
    void update_group_status_to_start_condition_2_group_status(GroupStatus status, GroupStatus expectedStatus) {
        //given
        Group group = givenGroupWithSettingStartDate(LocalDate.now());
        group.changeGroupStatusTo(status);
        Group savedGroup = groupRepository.save(group);

        //when
        groupRepository.updateGroupStatusToStart();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(expectedStatus);
    }

    @DisplayName("updateGroupStatusToStart() 메서드는 그룹 상태(GroupStatus)를 START로 변경한다. " +
            "그룹 시작일이 당일이 아니면 그룹 상태는 START로 변경되지 않고 현재 상태를 유지한다. ")
    @Test
    void update_group_status_to_start_condition_1_start_date_is_not_today() {
        //given
        Group group = givenGroupWithSettingStartDate(LocalDate.of(9999,12,31));
        Group savedGroup = groupRepository.save(group);

        //when
        groupRepository.updateGroupStatusToStart();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(GATHERING);
    }

    @DisplayName("updateGroupStatusToStart() 메서드는 그룹 상태(GroupStatus)를 START로 변경한다. " +
            "그룹 시작일이 당일이면 그룹 상태는 START로 변경된다. ")
    @Test
    void update_group_status_to_start_condition_1_start_date_is_today() {
        //given
        Group group = givenGroupWithSettingStartDate(LocalDate.now());
        Group savedGroup = groupRepository.save(group);

        //when
        groupRepository.updateGroupStatusToStart();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(START);
    }

    @Disabled("h2 미지원 함수")
    @DisplayName("updateGroupStatusToEnd() 메서드는 그룹 상태(GroupStatus)를 END로 변경한다. " +
            "조건 " +
            "1. 그룹 상태는 START 여야한다." +
            "2. 해당 메서드를 실행하는 시점은 정확히 그룹 스터디 종료일 + 3일이여야 그룹 상태가 END로 변경된다." +
            "해당 단위 테스트에서는 1번 조건을 테스트한다.")
    @ParameterizedTest(name = "[{index}] 현재 그룹 상태 : {0} | 메서드 실행 후 그룹 상태 : {1}")
    @CsvSource(value = {"GATHERING, GATHERING", "GATHER_COMPLETE, GATHER_COMPLETE", "START, END"})
    void update_group_status_to_end_condition_2_group_status(GroupStatus status, GroupStatus expectedStatus) {
        //given
        LocalDate threeDaysAfterFromNow = LocalDate.now().minusDays(3); //종료일을 현재 날짜에서 3일 전으로 설정
        Group group = givenGroupWithSettingEndDate(threeDaysAfterFromNow);
        group.changeGroupStatusTo(status);
        Group savedGroup = groupRepository.save(group);

        //when
        groupRepository.updateGroupStatusToEnd();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(expectedStatus);
    }

    @Disabled("h2 미지원 함수")
    @DisplayName("updateGroupStatusToEnd() 메서드 실행 시, " +
            "해당 메서드를 실행하는 시점이 정확히 그룹 스터디 종료일 + 3일이 아니라면 그룹 상태는 기존 상태로 유지된다. ")
    @ParameterizedTest(name = "[{index}]  그룹 종료일 {0} 일 후 메서드를 실행시켰을 때")
    @ValueSource(ints = {4, 2, -1})
    void update_group_status_to_end_condition_1_not_three_day_before_the_end_date(int dayToSubtract) {
        //given
        LocalDate threeDaysAfterFromNow = LocalDate.now().minusDays(dayToSubtract); //종료일을 현재 날짜에서 2일 전으로 설정
        Group group = givenGroupWithSettingEndDate(threeDaysAfterFromNow);
        Group savedGroup = groupRepository.save(group);
        //when
        groupRepository.updateGroupStatusToEnd();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(GATHERING);
    }

    private Group givenGroupWithSettingStartDate(LocalDate startDate) {
        return new Group(Period.of(startDate, LocalDate.of(9999, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID", "자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
    }

    private Group givenGroupWithSettingEndDate(LocalDate endDate) {
        return new Group(Period.of(LocalDate.of(2020, 12, 31), endDate),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID", "자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
    }
}