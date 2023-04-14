package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;

@ServiceTest
class GroupManagingServiceTest {

    @Autowired
    GroupManagingService groupManagingService;
    @Autowired
    GroupRepository groupRepository;

    Group findGroup;

    @BeforeEach
    void beforeEach() {
        Group group = TestGroupServiceSupporter.receiveSampleGroup(1l);
        groupRepository.save(group);
        findGroup = groupRepository.findAll().get(0);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }

    @DisplayName("서비스 레이어 그룹 가입 성공 케이스")
    @Test
    void group_join_success() {
        //given
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.receiveSampleMemberDetails(2l);

        //when - then
        Assertions.assertThatCode(() -> groupManagingService.join(memberDetails, findGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("서비스 레이어 그룹 가입 실패 케이스 " +
            "존재하지 않는 그룹 가입 시도")
    @Test
    void group_join_fail() {
        //given
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.receiveSampleMemberDetails(1l);

        //when - then
        Assertions.assertThatThrownBy(() -> groupManagingService.join(memberDetails, findGroup.getId() + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 그룹입니다.");
    }

    @DisplayName("서비스 레이어 그룹 모집 완료 케이스")
    @Test
    void group_close_recruitment_success() {
        //given
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.receiveSampleMemberDetails(2l);

        //when - then
        Assertions.assertThatCode(() -> groupManagingService.join(memberDetails, findGroup.getId()))
                .doesNotThrowAnyException();
    }

    @DisplayName("서비스 레이어 그룹 모집 완료 실패 케이스 " +
            "존재하지 않는 그룹 가입 시도")
    @Test
    void group_close_recruitment_fail() {
        //given
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.receiveSampleMemberDetails(1l);

        //when - then
        Assertions.assertThatThrownBy(() -> groupManagingService.closeRecruitment(memberDetails, findGroup.getId() + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXISTED_GROUP);
    }

}