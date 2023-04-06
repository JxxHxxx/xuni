package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.support.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_CLOSE_RECRUITMENT;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_JOIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GroupControllerTestConfig.class)
class GroupJoinControllerTest extends ControllerTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Long memberId = null;
    //given
    String testToken;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .id(1L)
                .loginInfo(LoginInfo.of("leesin5498@naver.com", "1234"))
                .name("자몽")
                .build();

        memberId = member.getId();
        testToken = jwtTokenProvider.issue(new SimpleMemberDetails(memberId, member.receiveEmail(), member.getName()));

    }

    @DisplayName("스터디 그룹 참여 성공 요청")
    @Test
    void group_join_request() throws Exception {
        //when - then
        mockMvc.perform(post("/groups/{group-id}/join",1l)
                .header("Authorization", testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_JOIN));

    }

    @DisplayName("스터디 그룹 모집 마감 성공 요청")
    @Test
    void group_close_request() throws Exception {
        //when - then
        mockMvc.perform(post("/groups/{group-id}/close-recruitment",1l)
                        .header("Authorization", testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_CLOSE_RECRUITMENT));
    }
}