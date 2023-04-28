package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jxx.xuni.group.domain.TestGroupServiceSupporter.*;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class GroupManagingServiceTest {

    @Autowired
    GroupManagingService groupManagingService;
    @Autowired
    GroupRepository groupRepository;

    Group findGroup;
    SimpleMemberDetails memberDetails;

    @BeforeEach
    void beforeEach() {
        Group group = receiveSampleGroup(1l);
        groupRepository.save(group);
        findGroup = groupRepository.findAll().get(0);
        memberDetails = receiveSampleMemberDetails(1l);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }

    @DisplayName("서비스 레이어 그룹 가입 성공 케이스")
    @Test
    void group_join_success() {
        //when - then
        SimpleMemberDetails anotherMemberDetails = UserMemberDetails(2l);
        assertThatCode(() -> groupManagingService.join(anotherMemberDetails, findGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("서비스 레이어 그룹 가입 실패 케이스 " +
            "존재하지 않는 그룹 가입 시도")
    @Test
    void group_join_fail() {
        //when - then
        assertThatThrownBy(() -> groupManagingService.join(memberDetails, findGroup.getId() + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 그룹입니다.");
    }

    @DisplayName("서비스 레이어 그룹 모집 완료 케이스")
    @Test
    void group_close_recruitment_success() {
        //when - then
        assertThatCode(() -> groupManagingService.closeRecruitment(memberDetails, findGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("서비스 레이어 그룹 모집 완료 실패 케이스 " +
            "존재하지 않는 그룹 가입 시도")
    @Test
    void group_close_recruitment_fail() {
        //when - then
        assertThatThrownBy(() -> groupManagingService.closeRecruitment(memberDetails, findGroup.getId() + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);
    }

    @DisplayName("서비스 레이어 스터디 챕터 체크 기능 성공 케이스")
    @Test
    void study_chapter_check_success() {
        Group saveGroup = groupRepository.save(startedGroupSample(memberDetails.getUserId(), 5));

        //when
        Long chapterId = saveGroup.getStudyChecks().get(0).getChapterId();
        //then
        assertThatCode(() -> groupManagingService.checkStudyChapter(memberDetails, saveGroup.getId(), chapterId))
                        .doesNotThrowAnyException();

    }

    @DisplayName("서비스 레이어 스터디 챕터 체크 기능 실패 케이스 - 존재하지 않는 그룹")
    @Test
    void study_chapter_check_fail_cause_not_exist_group() {
        //given
        Group saveGroup = groupRepository.save(startedGroupSample(memberDetails.getUserId(), 5));
        //when - then
        Long notExistGroupId = saveGroup.getId() + 1;
        assertThatThrownBy(() -> groupManagingService.checkStudyChapter(memberDetails, notExistGroupId,
                        saveGroup.getStudyChecks().get(0).getChapterId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);
    }
}