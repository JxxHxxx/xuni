package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.auth.domain.LoginInfo;
import com.jxx.xuni.auth.domain.Member;
import com.jxx.xuni.auth.domain.MemberRepository;
import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.jxx.xuni.auth.domain.AuthProvider.XUNI;
import static org.assertj.core.api.Assertions.*;

@ServiceTest
class GroupCreateServiceTest extends ServiceCommon {

    @Autowired
    GroupCreateService groupCreateService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberRepository memberRepository;

    Long MemberId;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(LoginInfo.of("leesin5498@naver.com", "12341234"), "재헌", XUNI));
        MemberId = member.getId();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @DisplayName("그룹 생성 서비스 성공 케이스")
    @Test
    void create_success() {
        //given
        GroupCreateForm groupCreateForm = new GroupCreateForm(
                "groupName",  // GroupName
                LocalDate.now(),  // startDate
                LocalDate.of(2099, 12, 31), // endDate
                LocalTime.MIDNIGHT,  // startTime
                LocalTime.NOON, // endTime
                10, // capacity
                "study-product-id", // studyProductId
                "JAVA 스터디", // subject - meaning studyProductName
                Category.JAVA);

        //when - then
        assertThatCode(() -> groupCreateService.create(MemberId, groupCreateForm))
                .doesNotThrowAnyException();

        assertThat(groupRepository.findAll().get(0)).isNotNull();
    }
}