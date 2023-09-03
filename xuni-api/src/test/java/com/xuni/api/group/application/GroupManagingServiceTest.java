package com.xuni.api.group.application;

import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.api.group.TestGroupServiceSupporter;
import com.xuni.core.group.domain.Group;
import com.xuni.api.group.infra.GroupRepository;
import com.xuni.core.group.domain.Task;
import com.xuni.api.support.ServiceCommon;
import com.xuni.api.support.ServiceTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.xuni.core.group.domain.GroupMessage.NOT_EXISTED_GROUP;
import static com.xuni.core.group.domain.GroupStatus.*;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class GroupManagingServiceTest extends ServiceCommon {

    @Autowired
    GroupManagingService groupManagingService;
    @Autowired
    GroupRepository groupRepository;

    Group findGroup;
    SimpleMemberDetails groupHostDetail;

    @BeforeEach
    void beforeEach() {
        Group group = TestGroupServiceSupporter.receiveSampleGroup(1l);
        groupRepository.save(group);
        findGroup = groupRepository.findAll().get(0);
        groupHostDetail = TestGroupServiceSupporter.receiveSampleMemberDetails(1l);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }

    @DisplayName("그룹 가입에 성공하면 " +
            "예외가 발생하지 않고 " +
            "가입을 신청한 사용자의 id, name 이 저장되어야 한다.")
    @Test
    void join_group_success() {
        //given
        SimpleMemberDetails anotherMemberDetails = TestGroupServiceSupporter.UserMemberDetails(2l);

        //when - then
        assertThatCode(() -> groupManagingService.join(anotherMemberDetails, findGroup.getId()))
                .doesNotThrowAnyException();

        Group group = groupRepository.findAll().get(0);
        List<Long> groupMemberIds = group.getGroupMembers().stream().map(g -> g.getGroupMemberId()).toList();
        assertThat(groupMemberIds).contains(2l);

        List<String> groupMemberNames = group.getGroupMembers().stream().map(g -> g.getGroupMemberName()).toList();
        assertThat(groupMemberNames).contains("재헌");
    }

    @DisplayName("서비스 레이어 그룹 가입 실패 케이스 " +
            "존재하지 않는 그룹에 가입 시도 시 {NOT_EXISTED_GROUP} 예외 발생")
    @Test
    void join_group_fail_cause_not_exist_group() {
        //given
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.UserMemberDetails(2l);
        long notExistGroupId = findGroup.getId() + 1;
        //when - then
        assertThatThrownBy(() -> groupManagingService.join(memberDetails, notExistGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);

    }

    @DisplayName("그룹 모집 완료에 성공하면 " +
            "어떠한 예외도 발생하지 않고 " +
            "데이터 베이스 내 해당 그룹의 상태는 GATHER_COMPLETE 이다.")
    @Test
    void close_recruitment_of_group_success() {
        //when - then
        assertThatCode(() -> groupManagingService.closeRecruitment(groupHostDetail, findGroup.getId()))
                .doesNotThrowAnyException();

        Group updateGroup = groupRepository.findById(findGroup.getId()).get();
        assertThat(updateGroup.getGroupStatus()).isEqualTo(GATHER_COMPLETE);
    }

    @DisplayName("서비스 레이어 그룹 모집 완료 실패 케이스 " +
            "존재하지 않는 그룹에 가입 시도 시 {NOT_EXISTED_GROUP} 예외 발생")
    @Test
    void close_recruitment_fail_cause_not_exist_group() {
        //given
        long notExistGroupId = findGroup.getId() + 1;
        //when - then
        assertThatThrownBy(() -> groupManagingService.closeRecruitment(groupHostDetail, notExistGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);
    }

    @DisplayName("스터디 챕터 체크 기능 성공하면 " +
            "어떠한 예외도 발생하지 않고 " +
            "데이터 베이스 내 해당 studyCheck 의 isDone 필드 값은 True로 변경된다.")
    @Test
    void check_study_chapter_success() {
        Group startedGroup = groupRepository.save(TestGroupServiceSupporter.startedGroupSample(groupHostDetail.getUserId(), 5));
        //when
        Long chapterId = startedGroup.getTasks().get(0).getChapterId();
        //then
        assertThatCode(() -> groupManagingService.doTask(groupHostDetail, startedGroup.getId(), chapterId))
                        .doesNotThrowAnyException();

        //then
        Group updateGroup = groupRepository.findById(startedGroup.getId()).get();
        Task firstStudyCheck = updateGroup.getTasks().get(0);
        assertThat(firstStudyCheck.isDone()).isTrue();

    }

    @DisplayName("서비스 레이어 스터디 챕터 체크 기능 실패 케이스" +
            "존재하지 않는 그룹에 가입 시도 시 {NOT_EXISTED_GROUP} 예외 발")
    @Test
    void check_study_chapter_fail_cause_not_exist_group() {
        //given
        Group saveGroup = groupRepository.save(TestGroupServiceSupporter.startedGroupSample(groupHostDetail.getUserId(), 5));
        //when - then
        Long notExistGroupId = saveGroup.getId() + 1;
        assertThatThrownBy(() -> groupManagingService.doTask(groupHostDetail, notExistGroupId,
                        saveGroup.getTasks().get(0).getChapterId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);
    }
}