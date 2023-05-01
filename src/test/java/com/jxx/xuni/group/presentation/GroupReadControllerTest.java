package com.jxx.xuni.group.presentation;

import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.group.dto.response.GroupReadOneResponse;
import com.jxx.xuni.studyproduct.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GroupControllerTestConfig.class)
class GroupReadControllerTest extends GroupCommon {

    @Autowired
    GroupReadService groupReadService;

    @DisplayName("스터디 그룹 전체 조회 요청 API")
    @Test
    void read_all_group() throws Exception {
        List<GroupMember> groupMembers = new ArrayList<>();
        groupMembers.add(new GroupMember(1l, "xuni-member"));

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
                List.of(new GroupMember(1l, "이재헌"), new GroupMember(12l, "김유니")));

        BDDMockito.given(groupReadService.readOne(1l)).willReturn(response);

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
                                fieldWithPath("response.groupMembers[].isLeft").type(JsonFieldType.BOOLEAN).description("탈퇴 여부")
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
                RestDocumentationRequestBuilders.get("/groups/cond")
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
}