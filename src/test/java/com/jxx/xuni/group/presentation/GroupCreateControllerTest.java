package com.jxx.xuni.group.presentation;

import com.jxx.xuni.ApiDocumentUtils;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.group.dto.response.GroupApiMessage;
import com.jxx.xuni.auth.domain.LoginInfo;
import com.jxx.xuni.auth.domain.Member;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.jxx.xuni.ApiDocumentUtils.*;
import static com.jxx.xuni.common.domain.Category.JAVA;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GroupControllerTestConfig.class)
public class GroupCreateControllerTest extends GroupCommon {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Long memberId = null;
    String authToken;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .id(1L)
                .loginInfo(LoginInfo.of("leesin5498@naver.com", "1234"))
                .name("자몽")
                .build();

        memberId = member.getId();
        authToken = jwtTokenProvider.issue(new SimpleMemberDetails(memberId, member.receiveEmail(), member.getName()));

    }

    @Test
    void group_create_docs() throws Exception {
        //given
        GroupCreateForm groupCreateForm = makeGroupCreateForm(5);
        //when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/groups")
                .header("Authorization", authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupCreateForm)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(GroupApiMessage.GROUP_CREATED))
                .andDo(MockMvcRestDocumentation.document("group/create",
                        getDocumentRequest(), getDocumentResponse(),

                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),

                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("리소스 생성 위치")
                        ),

                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("그룹 이름"),
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("스터디 주제"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("스터디 시작일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("스터디 종료일"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING).description("스터디 시작 시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING).description("스터디 종료 시간"),
                                fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("그룹 정원"),
                                fieldWithPath("studyProductId").type(JsonFieldType.STRING).description("스터디 주제 식별자"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("스터디 카테고리")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )

                ));
    }

    @DisplayName("스터디 그룹 생성 성공 케이스")
    @Test
    void create_success() throws Exception {
        //given
        GroupCreateForm groupCreateForm = makeGroupCreateForm(5);
        //when
        mockMvc.perform(post("/groups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupCreateForm)))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(GroupApiMessage.GROUP_CREATED));
    }

    @DisplayName("스터디 그룹의 인원의 범위는 1 이상 5 이하이다. 그 외의 값을 입력할 경우 " +
            "컨트롤러에서는 " +
            "400 상태 코드 " +
            "{FAIL_MESSAGE} 메시지를 반환한다.")
    @Disabled(value = "실패 케이스는 인수 테스트에서 실행한다.")
    @Test
    void create_fail1() throws Exception {
        //given
        GroupCreateForm groupCreateForm = makeGroupCreateForm(-1);
        //when - then
        String requestBody = objectMapper.writeValueAsString(groupCreateForm);
        mockMvc.perform(post("/groups")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(jsonPath("$.message").value(GroupApiMessage.GROUP_UNCREATED));
    }

    private GroupCreateForm makeGroupCreateForm(Integer capacity) {
        return new GroupCreateForm(
                "groupName",
                LocalDate.now(),
                LocalDate.of(2023, 7, 15),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                capacity,
                "study-product-id",
                "자바의 정석",
                JAVA);
    }
}
