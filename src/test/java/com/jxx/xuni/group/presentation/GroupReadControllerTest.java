package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.group.dto.response.*;
import com.jxx.xuni.group.query.converter.PageConverter;
import com.jxx.xuni.common.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GroupControllerTestConfig.class)
class GroupReadControllerTest extends GroupCommon {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    GroupReadService groupReadService;
    @Autowired
    PageConverter pageConverter;

    @DisplayName("스터디 그룹 전체 조회 요청 API")
    @Test
    void read_all_group() throws Exception {
        List<GroupMember> groupMembers = new ArrayList<>();
        groupMembers.add(new GroupMember(1l, "xuni-member", null));

        GroupReadAllResponse response1 = new GroupReadAllResponse(
                1l,
                new Capacity(10),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member1"),
                Study.of("UUID", "JAVA의 정석", Category.JAVA),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.now(), LocalDate.of(2123, 12, 31)));

        GroupReadAllResponse response2 = new GroupReadAllResponse(
                2l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(2l, "xuni-member2"),
                Study.of("UUID", "Real MySQL 8.0 (1권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.now(), LocalDate.of(2123, 12, 31)));

        BDDMockito.given(groupReadService.readAll()).willReturn(List.of(response1, response2));

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/groups")
                        .contentType(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_ALL_READ))

                .andDo(MockMvcRestDocumentation.document("group/query/readAll",
                        getDocumentRequest(), getDocumentResponse(),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].groupId").type(JsonFieldType.NUMBER).description("그룹 식별자"),

                                fieldWithPath("response[].capacity").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response[].capacity.totalCapacity").type(JsonFieldType.NUMBER).description("정원"),
                                fieldWithPath("response[].capacity.leftCapacity").type(JsonFieldType.NUMBER).description("남은 자리"),

                                fieldWithPath("response[].time").type(JsonFieldType.OBJECT).description("그룹 스터디 시간"),
                                fieldWithPath("response[].time.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("response[].time.endTime").type(JsonFieldType.STRING).description("종료 시간"),

                                fieldWithPath("response[].period").type(JsonFieldType.OBJECT).description("그룹 스터디 기간"),
                                fieldWithPath("response[].period.startDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("response[].period.endDate").type(JsonFieldType.STRING).description("종료일"),

                                fieldWithPath("response[].groupStatus").type(JsonFieldType.STRING).description("그룹 상태"),

                                fieldWithPath("response[].host").type(JsonFieldType.OBJECT).description("호스트"),
                                fieldWithPath("response[].host.hostId").type(JsonFieldType.NUMBER).description("호스트 식별자"),
                                fieldWithPath("response[].host.hostName").type(JsonFieldType.STRING).description("호스트 이름"),

                                fieldWithPath("response[].study").type(JsonFieldType.OBJECT).description("스터디"),
                                fieldWithPath("response[].study.id").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].study.subject").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("response[].study.category").type(JsonFieldType.STRING).description("스터디 카테고리")
                            )
                ));
    }

    @DisplayName("스터디 그룹 단일 조회 요청 API")
    @Test
    void read_one_group() throws Exception {

        GroupReadOneResponse response = new GroupReadOneResponse(
                1l,
                new Capacity(10),
                GroupStatus.GATHERING,
                new Host(1l, "이재헌"),
                Study.of("UUID", "JAVA의 정석", Category.JAVA),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.now(), LocalDate.of(2123, 12, 31)),
                List.of(new GroupMemberDto(1l, "이재헌" , false , LocalDateTime.now()),
                        new GroupMemberDto(12l, "김유니", false, LocalDateTime.now())
                ));

        BDDMockito.given(groupReadService.readOne(any(), any())).willReturn(response);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/groups/{group-id}", "1")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_ONE_READ))

                .andDo(MockMvcRestDocumentation.document("group/query/readOne",
                        getDocumentRequest(), getDocumentResponse(),

                        pathParameters(
                                parameterWithName("group-id").description("그룹 식별자")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response.groupId").type(JsonFieldType.NUMBER).description("그룹 식별자"),

                                fieldWithPath("response.capacity").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response.capacity.totalCapacity").type(JsonFieldType.NUMBER).description("정원"),
                                fieldWithPath("response.capacity.leftCapacity").type(JsonFieldType.NUMBER).description("남은 자리"),

                                fieldWithPath("response.time").type(JsonFieldType.OBJECT).description("그룹 스터디 시간"),
                                fieldWithPath("response.time.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("response.time.endTime").type(JsonFieldType.STRING).description("종료 시간"),

                                fieldWithPath("response.period").type(JsonFieldType.OBJECT).description("그룹 스터디 기간"),
                                fieldWithPath("response.period.startDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("response.period.endDate").type(JsonFieldType.STRING).description("종료일"),

                                fieldWithPath("response.groupStatus").type(JsonFieldType.STRING).description("그룹 상태"),

                                fieldWithPath("response.host").type(JsonFieldType.OBJECT).description("호스트"),
                                fieldWithPath("response.host.hostId").type(JsonFieldType.NUMBER).description("호스트 식별자"),
                                fieldWithPath("response.host.hostName").type(JsonFieldType.STRING).description("호스트 이름"),

                                fieldWithPath("response.study").type(JsonFieldType.OBJECT).description("스터디"),
                                fieldWithPath("response.study.id").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response.study.subject").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("response.study.category").type(JsonFieldType.STRING).description("스터디 카테고리"),


                                fieldWithPath("response.groupMembers[]").type(JsonFieldType.ARRAY).description("그룹 멤버"),
                                fieldWithPath("response.groupMembers[].groupMemberId").type(JsonFieldType.NUMBER).description("그룹 멤버 식별자"),
                                fieldWithPath("response.groupMembers[].groupMemberName").type(JsonFieldType.STRING).description("그룹 멤버 이름"),
                                fieldWithPath("response.groupMembers[].isLeft").type(JsonFieldType.BOOLEAN).description("탈퇴 여부"),
                                fieldWithPath("response.groupMembers[].lastVisitedTime").type(JsonFieldType.STRING).description("마지막 방문 시간")
                        )
                ));
    }

    @DisplayName("스터디 그룹 카테고리 별 조회 요청 API")
    @Test
    void read_by_category_group() throws Exception {
        GroupReadAllResponse response1 = new GroupReadAllResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (1권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.now(), LocalDate.of(2123, 12, 31)));

        GroupReadAllResponse response2 = new GroupReadAllResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (2권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.now(), LocalDate.of(2123, 12, 31)));

        BDDMockito.given(groupReadService.readByCategory(Category.MYSQL)).willReturn(List.of(response1, response2));

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/groups/cd-sp")
                        .param("category", Category.MYSQL.name())
                        .contentType(MediaType.APPLICATION_JSON));
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_CATEGORY_READ))

                .andDo(MockMvcRestDocumentation.document("group/query/readCond",
                        getDocumentRequest(), getDocumentResponse(),

                        queryParameters(
                                parameterWithName("category").description("스터디 상품 카테고리")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].groupId").type(JsonFieldType.NUMBER).description("그룹 식별자"),

                                fieldWithPath("response[].capacity").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response[].capacity.totalCapacity").type(JsonFieldType.NUMBER).description("정원"),
                                fieldWithPath("response[].capacity.leftCapacity").type(JsonFieldType.NUMBER).description("남은 자리"),

                                fieldWithPath("response[].time").type(JsonFieldType.OBJECT).description("그룹 스터디 시간"),
                                fieldWithPath("response[].time.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("response[].time.endTime").type(JsonFieldType.STRING).description("종료 시간"),

                                fieldWithPath("response[].period").type(JsonFieldType.OBJECT).description("그룹 스터디 기간"),
                                fieldWithPath("response[].period.startDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("response[].period.endDate").type(JsonFieldType.STRING).description("종료일"),

                                fieldWithPath("response[].groupStatus").type(JsonFieldType.STRING).description("그룹 상태"),

                                fieldWithPath("response[].host").type(JsonFieldType.OBJECT).description("호스트"),
                                fieldWithPath("response[].host.hostId").type(JsonFieldType.NUMBER).description("호스트 식별자"),
                                fieldWithPath("response[].host.hostName").type(JsonFieldType.STRING).description("호스트 이름"),

                                fieldWithPath("response[].study").type(JsonFieldType.OBJECT).description("스터디"),
                                fieldWithPath("response[].study.id").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].study.subject").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("response[].study.category").type(JsonFieldType.STRING).description("스터디 카테고리")
                        )
                ));
    }

    @DisplayName("스터디 그룹 조건 조회 API")
    @Test
    void search_group() throws Exception {
        GroupAllQueryResponse response1 = new GroupAllQueryResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (1권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.of(2123, 5, 1), LocalDate.of(2123, 12, 31)));

        GroupAllQueryResponse response2 = new GroupAllQueryResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (2권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.of(2123, 6, 1), LocalDate.of(2123, 12, 31)));
        Pageable pageable = Pageable.ofSize(20);

        List<GroupAllQueryResponse> content = List.of(response1, response2);

        BDDMockito.given(groupReadService.searchGroup(any(), any()))
                .willReturn(new PageImpl<>(content, pageable, content.size()));

        BDDMockito.given(pageConverter.toPageInfo(any(), anyLong(), anyInt()))
                .willReturn(PageInfo.of(0, 20, 1, 2, 1, true));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("/groups/cd-cp")
                .param("category", Category.MYSQL.name())
                .param("readType", "default")
                .param("isAsc", "true")
                .param("sortProperty", "start-date")
                .param("page", "0")
                .param("subject","")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SEARCH_GROUP_COND))

                .andDo(MockMvcRestDocumentation.document("group/query/dynamic",
                        getDocumentRequest(), getDocumentResponse(),

                        queryParameters(
                                parameterWithName("category").description("스터디 주제"),
                                parameterWithName("readType").description("조회할 그룹 상태"),
                                parameterWithName("isAsc").description("오름차순 여부"),
                                parameterWithName("sortProperty").description("정렬 기준이 되는 속성"),
                                parameterWithName("subject").description("검색 입력어 (스터디 주제 이름)"),
                                parameterWithName("page").description("현재 페이지")
                                ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].groupId").type(JsonFieldType.NUMBER).description("그룹 식별자"),

                                fieldWithPath("response[].capacity").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response[].capacity.totalCapacity").type(JsonFieldType.NUMBER).description("정원"),
                                fieldWithPath("response[].capacity.leftCapacity").type(JsonFieldType.NUMBER).description("남은 자리"),

                                fieldWithPath("response[].time").type(JsonFieldType.OBJECT).description("그룹 스터디 시간"),
                                fieldWithPath("response[].time.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("response[].time.endTime").type(JsonFieldType.STRING).description("종료 시간"),

                                fieldWithPath("response[].period").type(JsonFieldType.OBJECT).description("그룹 스터디 기간"),
                                fieldWithPath("response[].period.startDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("response[].period.endDate").type(JsonFieldType.STRING).description("종료일"),

                                fieldWithPath("response[].groupStatus").type(JsonFieldType.STRING).description("그룹 상태"),

                                fieldWithPath("response[].host").type(JsonFieldType.OBJECT).description("호스트"),
                                fieldWithPath("response[].host.hostId").type(JsonFieldType.NUMBER).description("호스트 식별자"),
                                fieldWithPath("response[].host.hostName").type(JsonFieldType.STRING).description("호스트 이름"),

                                fieldWithPath("response[].study").type(JsonFieldType.OBJECT).description("스터디"),
                                fieldWithPath("response[].study.id").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].study.subject").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("response[].study.category").type(JsonFieldType.STRING).description("스터디 카테고리"),

                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageInfo.offset").type(JsonFieldType.NUMBER).description("가져와야 할 레코드의 인덱스"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("한 페이지에 표시할 레코드 수"),
                                fieldWithPath("pageInfo.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 레코드 수"),
                                fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                            )
                    ));
    }

    @DisplayName("그룹 스터디 체크 조회 API")
    @Test
    void read_chapter() throws Exception {
        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.UserMemberDetails(1l);

        GroupStudyCheckResponse response1 = new GroupStudyCheckResponse(1l, "인터페이스", true);
        GroupStudyCheckResponse response2 = new GroupStudyCheckResponse(2l, "객체 지향 설계", false);

        BDDMockito.given(groupReadService.readGroupTask(any(), any())).willReturn(List.of(response1, response2));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/groups/{group-id}/chapters", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtTokenProvider.issue(memberDetails)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(STUDY_CHECK_OF_GROUP_MEMBER))
                .andDo(
                        MockMvcRestDocumentation.document("group/query/studyCheck",
                                getDocumentRequest(), getDocumentResponse(),

                                HeaderDocumentation.requestHeaders(
                                        HeaderDocumentation.headerWithName("Authorization").description("인증 토큰")
                                ),

                                pathParameters(
                                        parameterWithName("group-id").description("그룹 식별자")
                                ),

                                PayloadDocumentation.responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                        fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                        fieldWithPath("response[].chapterId").type(JsonFieldType.NUMBER).description("챕터 식별자"),
                                        fieldWithPath("response[].title").type(JsonFieldType.STRING).description("챕터 제목"),
                                        fieldWithPath("response[].done").type(JsonFieldType.BOOLEAN).description("수행 여부")
                                )
                        )
                );

    }

    @DisplayName("내가 속한 그룹 조회 API")
    @Test
    void read_own_by_self() throws Exception {
        GroupAllQueryResponse response1 = new GroupAllQueryResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (1권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.of(2123, 5, 1), LocalDate.of(2123, 12, 31)));

        GroupAllQueryResponse response2 = new GroupAllQueryResponse(
                1l,
                new Capacity(5),
                GroupStatus.GATHERING,
                new Host(1l, "xuni-member"),
                Study.of("UUID", "Real MySQL 8.0 (2권)", Category.MYSQL),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                Period.of(LocalDate.of(2123, 6, 1), LocalDate.of(2123, 12, 31)));

        List<GroupAllQueryResponse> responses = List.of(response1, response2);

        BDDMockito.given(groupReadService.readOwn(any())).willReturn(responses);

        SimpleMemberDetails memberDetails = TestGroupServiceSupporter.receiveSampleMemberDetails(1l);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/members/{member-id}/groups", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwtTokenProvider.issue(memberDetails)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(GROUP_OWN_READ))

                .andDo(MockMvcRestDocumentation.document("group/query/readOwn",

                        pathParameters(
                                parameterWithName("member-id").description("회원 식별자")
                        ),

                        HeaderDocumentation.requestHeaders(
                                HeaderDocumentation.headerWithName("Authorization").description("인증 토큰")
                        ),

                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response[].groupId").type(JsonFieldType.NUMBER).description("그룹 식별자"),

                                fieldWithPath("response[].capacity").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response[].capacity.totalCapacity").type(JsonFieldType.NUMBER).description("정원"),
                                fieldWithPath("response[].capacity.leftCapacity").type(JsonFieldType.NUMBER).description("남은 자리"),

                                fieldWithPath("response[].time").type(JsonFieldType.OBJECT).description("그룹 스터디 시간"),
                                fieldWithPath("response[].time.startTime").type(JsonFieldType.STRING).description("시작 시간"),
                                fieldWithPath("response[].time.endTime").type(JsonFieldType.STRING).description("종료 시간"),

                                fieldWithPath("response[].period").type(JsonFieldType.OBJECT).description("그룹 스터디 기간"),
                                fieldWithPath("response[].period.startDate").type(JsonFieldType.STRING).description("시작일"),
                                fieldWithPath("response[].period.endDate").type(JsonFieldType.STRING).description("종료일"),

                                fieldWithPath("response[].groupStatus").type(JsonFieldType.STRING).description("그룹 상태"),

                                fieldWithPath("response[].host").type(JsonFieldType.OBJECT).description("호스트"),
                                fieldWithPath("response[].host.hostId").type(JsonFieldType.NUMBER).description("호스트 식별자"),
                                fieldWithPath("response[].host.hostName").type(JsonFieldType.STRING).description("호스트 이름"),

                                fieldWithPath("response[].study").type(JsonFieldType.OBJECT).description("스터디"),
                                fieldWithPath("response[].study.id").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].study.subject").type(JsonFieldType.STRING).description("스터디 이름"),
                                fieldWithPath("response[].study.category").type(JsonFieldType.STRING).description("스터디 카테고리")

                        )
                ));
    }

}