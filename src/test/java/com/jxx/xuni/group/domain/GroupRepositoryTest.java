package com.jxx.xuni.group.domain;

import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.domain.MemberRepository;
import com.jxx.xuni.studyproduct.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
    void update_group_status_to_start_condition_1(GroupStatus status, GroupStatus expectedStatus) {
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
    void update_group_status_to_start_condition_2_not_change() {
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
    void update_group_status_to_start_condition_2_change() {
        //given
        Group group = givenGroupWithSettingStartDate(LocalDate.now());
        Group savedGroup = groupRepository.save(group);

        //when
        groupRepository.updateGroupStatusToStart();

        //then
        Group findGroup = groupRepository.findById(savedGroup.getId()).get();
        Assertions.assertThat(findGroup.getGroupStatus()).isEqualTo(START);
    }

    private Group givenGroupWithSettingStartDate(LocalDate startDate) {
        return new Group(Period.of(startDate, LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID", "자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
    }
}