package com.jxx.xuni.group.application;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupCreator;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.support.ServiceTest;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class GroupJoinServiceTest {

    @Autowired
    GroupJoinService groupJoinService;
    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    void beforeEach() {
        Group group = GroupCreator.receiveBasicSample();
        groupRepository.save(group);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }

    @DisplayName("서비스 레이어 그룹 가입 성공 케이스")
    void group_join_success() {
        //given
        MemberDetails memberDetails = new SimpleMemberDetails(2l, "leesin5498@naver.com", "재헌");

        //when - then
        Assertions.assertThatCode(() -> groupJoinService.join(memberDetails, 1l))
                .doesNotThrowAnyException();
    }

    @DisplayName("서비스 레이어 그룹 가입 성공 실패 케이스")
    void group_join_fail() {
        //given
        MemberDetails memberDetails = new SimpleMemberDetails(2l, "leesin5498@naver.com", "재헌");

        //when - then
        Assertions.assertThatThrownBy(() -> groupJoinService.join(memberDetails, 2l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 그룹입니다.");
    }

}