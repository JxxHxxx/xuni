package com.xuni.api.group.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuni.auth.application.SimpleMemberDetails;
import com.xuni.auth.domain.AuthProvider;
import com.xuni.auth.domain.LoginInfo;
import com.xuni.auth.domain.Member;
import com.xuni.auth.domain.MemberRepository;
import com.xuni.auth.support.JwtTokenProvider;
import com.xuni.common.domain.Category;
import com.xuni.group.dto.request.GroupCreateForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TODO : 그룹 생성 시 필드 유효성 체크해야함

@SpringBootTest
@AutoConfigureMockMvc
public class GroupApiExceptionTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;
    Long memberId;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(
                LoginInfo.of("xuni@xuni.com", "0000000"),
                "유니",
                AuthProvider.XUNI));
        memberId = member.getId();
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
    }

    @DisplayName("그룹 생성 요청 시 필수 값을 지정하지 않은 채 요청을 보낼 경우 " +
            "400 상태 코드" +
            "'종료 시간을 지정하십시오' 메시지를 응답한다.")
    @Test
    void create_group_exp_cause_required_value_is_empty() throws Exception {
        GroupCreateForm form = new GroupCreateForm(
                "groupName",
                LocalDate.now(),
                LocalDate.now().plusDays(10l),
                LocalTime.MIDNIGHT,
                null, // 종료 시간 미기입
                5,
                "study-product-idx",
                "책 제목",
                Category.JAVA);

        mockMvc.perform(MockMvcRequestBuilders.post("/groups")
                        .header("Authorization", jwtTokenProvider.issue(new SimpleMemberDetails(memberId, "xuni@xuni.com", "유니")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("종료 시간을 지정하십시오"));
    }
}
