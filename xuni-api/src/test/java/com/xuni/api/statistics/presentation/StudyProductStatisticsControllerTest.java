package com.xuni.api.statistics.presentation;

import com.xuni.api.auth.application.jwt.JwtTokenProvider;
import com.xuni.api.statistics.application.StudyProductStatisticsService;
import com.xuni.api.statistics.dto.response.StudyProductStatisticsReadResponse;
import com.xuni.api.support.ControllerCommon;
import com.xuni.api.support.JwtTestConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.xuni.api.statistics.dto.response.StatisticsApiMessage.STUDY_PRODUCT_STAT_READ_ALL;
import static com.xuni.api.statistics.dto.response.StatisticsApiMessage.STUDY_PRODUCT_STAT_READ_ONE;
import static java.nio.charset.StandardCharsets.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({StatisticsControllerTestConfig.class, JwtTestConfiguration.class})
class StudyProductStatisticsControllerTest extends ControllerCommon  {

    @Autowired
    JwtTokenProvider testJwtTokenProvider;
    @Autowired
    StudyProductStatisticsService studyProductStatisticsService;

    @Test
    void read_all_docs() throws Exception {
        List<StudyProductStatisticsReadResponse> response = List.of(
                new StudyProductStatisticsReadResponse(UUID.randomUUID().toString(), 125, 25),
                new StudyProductStatisticsReadResponse(UUID.randomUUID().toString(), 0, 0)
        );
        BDDMockito.given(studyProductStatisticsService.readBy(any())).willReturn(response);

        ResultActions result = mockMvc.perform(get("/statistics/study-products")
                .param("page", "0")
                .param("size","10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(STUDY_PRODUCT_STAT_READ_ALL))

                .andDo(MockMvcRestDocumentation.document("statistics/study_product/read_all",

                        queryParameters(
                                parameterWithName("page").description("현재 페이지"),
                                parameterWithName("size").description("페이지에 보여줄 데이터 개수")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].studyProductId").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].ratingSum").type(JsonFieldType.NUMBER).description("평점 합계"),
                                fieldWithPath("response[].reviewCnt").type(JsonFieldType.NUMBER).description("리뷰 개수")
                        )
                ));
    }

    @Test
    void read_one() throws Exception {
        String studyProductId = UUID.randomUUID().toString();
        StudyProductStatisticsReadResponse response = new StudyProductStatisticsReadResponse(studyProductId, 125, 25);
        BDDMockito.given(studyProductStatisticsService.readOne(anyString())).willReturn(response);

        ResultActions result = mockMvc.perform(get("/statistics/study-products/{study-product-id}", studyProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(STUDY_PRODUCT_STAT_READ_ONE))

                .andDo(MockMvcRestDocumentation.document("statistics/study_product/read_one",

                        pathParameters(
                                parameterWithName("study-product-id").description("스터디 상품 식별자")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response.studyProductId").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response.ratingSum").type(JsonFieldType.NUMBER).description("평점 합계"),
                                fieldWithPath("response.reviewCnt").type(JsonFieldType.NUMBER).description("리뷰 개수")
                        )
                ));
    }
}