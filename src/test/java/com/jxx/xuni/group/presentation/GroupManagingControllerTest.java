package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.group.dto.request.StudyCheckForm;
import com.jxx.xuni.member.domain.LoginInfo;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.support.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(GroupControllerTestConfig.class)
class GroupManagingControllerTest extends ControllerTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Long memberId = null;
    //given
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

    @DisplayName("스터디 그룹 참여 요청 성공")
    @Test
    void group_join_request() throws Exception {
        //when - then
        ResultActions result = mockMvc.perform(post
                        ("/groups/{group-id}/join", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_JOIN))
                .andDo(MockMvcRestDocumentation.document("group/join",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                RequestDocumentation.parameterWithName("group-id").description("그룹 식별자")),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                )));

    }

    @DisplayName("스터디 그룹 탈퇴 요청 성공")
    @Test
    void group_leave_api() throws Exception {
        ResultActions result = mockMvc.perform(patch
                ("/groups/{group-id}/leave", 1l)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(LEAVE_GROUP))
                .andDo(MockMvcRestDocumentation.document("group/leave",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                RequestDocumentation.parameterWithName("group-id").description("그룹 식별자")),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )));
    }

    @DisplayName("스터디 그룹 시작 요청 성공")
    @Test
    void group_start_api() throws Exception {
        List<StudyCheckForm> studyCheckForms = TestGroupServiceSupporter.studyCheckForms;
        ResultActions result = mockMvc.perform(post
                ("/groups/{group-id}/start", 1l)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studyCheckForms))
                .header("Authorization", authToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_START))
                .andDo(MockMvcRestDocumentation.document("group/start",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                RequestDocumentation.parameterWithName("group-id").description("그룹 식별자")),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )));

    }

    @DisplayName("스터디 그룹 모집 마감 요청 성공")
    @Test
    void group_close_request() throws Exception {
        //when - then
        ResultActions result = mockMvc.perform(patch("/groups/{group-id}/close-recruitment", 1l)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_CLOSE_RECRUITMENT))
                .andDo(MockMvcRestDocumentation.document("group/close",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                RequestDocumentation.parameterWithName("group-id").description("그룹 식별자")),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )));
    }

    @DisplayName("스터디 챕터 체크 요청 성공")
    @Test
    void group_check_study_chapter() throws Exception {
        //when - then
        ResultActions result = mockMvc.perform(patch("/groups/{group-id}/chapters/{chapter-id}", 1l, 1l)
                .header("Authorization", authToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(DO_CHECK))
                .andDo(MockMvcRestDocumentation.document("group/check",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                RequestDocumentation.parameterWithName("group-id").description("그룹 식별자"),
                                RequestDocumentation.parameterWithName("chapter-id").description("스터디 챕터 식별자")),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )));
    }
}