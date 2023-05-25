package com.jxx.xuni.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.review.application.ReviewService;
import com.jxx.xuni.review.domain.Progress;
import com.jxx.xuni.review.dto.request.ReviewForm;
import com.jxx.xuni.review.dto.response.ReviewOneResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.review.dto.response.ReviewApiMessage.REVIEW_CREATE;
import static com.jxx.xuni.review.dto.response.ReviewApiMessage.REVIEW_READ;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ReviewControllerTestConfig.class)
class ReviewControllerTest extends ReviewCommon {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ObjectMapper objectMapper;

    String studyProductId = "study-product-identifier";

    @Test
    void create_review_docs() throws Exception {
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        ReviewForm form = new ReviewForm((byte) 3, "ORM 기초를 쌓는데 정말 유익한 것 같아요", 50);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/reviews/study-products/{study-product-id}", studyProductId)
                .header("Authorization", jwtTokenProvider.issue(memberDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)));

        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(REVIEW_CREATE))

                .andDo(MockMvcRestDocumentation.document("review/create",
                        requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                        ),

                        pathParameters(
                                parameterWithName("study-product-id").description("스터디 상품 식별자")
                        ),

                        requestFields(
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점"),
                                fieldWithPath("comment").type(JsonFieldType.STRING).description("한 줄 평"),
                                fieldWithPath("progress").type(JsonFieldType.NUMBER).description("스터디 상품 진행률 [API 호출을 통해 불러온다]")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    void read_review_by_docs() throws Exception {
        ReviewOneResponse response1 = new ReviewOneResponse(
                1l,
                "ORM 기초를 배우는데 좋은 것 같습니다.",
                (byte) 3, LocalDateTime.now(),
                10l,
                "유니",
                Progress.HALF);

        ReviewOneResponse response2 = new ReviewOneResponse(
                1l,
                "김영한 그는 JPA의 신이야",
                (byte) 3, LocalDateTime.of(2023,5, 15, 10, 20),
                15l,
                "허니",
                Progress.ALMOST);

        List<ReviewOneResponse> responses = List.of(response1, response2);

        BDDMockito.given(reviewService.read(any())).willReturn(responses);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/study-products/{study-product-id}", studyProductId)
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(REVIEW_READ))

                .andDo(
                        MockMvcRestDocumentation.document("review/read",
                                getDocumentRequest(), getDocumentResponse(),

                                pathParameters(
                                        parameterWithName("study-product-id").description("스터디 상품 식별자")
                                ),

                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                        fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                        fieldWithPath("response[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 식별자"),
                                        fieldWithPath("response[].comment").type(JsonFieldType.STRING).description("한 줄 평"),
                                        fieldWithPath("response[].rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("response[].lastModifiedTime").type(JsonFieldType.STRING).description("마지막 수정일"),
                                        fieldWithPath("response[].reviewerId").type(JsonFieldType.NUMBER).description("리뷰어 식별자"),
                                        fieldWithPath("response[].reviewerName").type(JsonFieldType.STRING).description("리뷰어 이름"),
                                        fieldWithPath("response[].progress").type(JsonFieldType.STRING).description("스터디 진행률")
                                )

                        )
                );
    }
}