package com.jxx.xuni.review.presentation;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.review.application.ReviewLikeService;
import com.jxx.xuni.review.domain.LikeStatus;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.review.dto.response.ReviewApiMessage.REVIEW_LIKE_CREATE;
import static com.jxx.xuni.review.dto.response.ReviewApiMessage.REVIEW_LIKE_UPDATE;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ReviewControllerTestConfig.class)
class ReviewLikeControllerTest extends ReviewCommon {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ReviewLikeService reviewLikeService;

    @Test
    void execute_docs_first_time() throws Exception {
        given(reviewLikeService.execute(anyLong(), anyLong())).willReturn(LikeStatus.INIT);

        Long reviewId = 1l;
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        mockMvc.perform(post("/reviews/{review-id}/like", reviewId)
                .header(AUTHORIZATION, jwtTokenProvider.issue(memberDetails))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(REVIEW_LIKE_CREATE))

                .andDo(document("review/like/first",
                        getDocumentRequest(), getDocumentResponse(),

                        HeaderDocumentation.requestHeaders(
                                headerWithName(AUTHORIZATION).description("인증 코드")
                        ),

                        RequestDocumentation.pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),

                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    void execute_docs_not_first_time() throws Exception {
        given(reviewLikeService.execute(anyLong(), anyLong())).willReturn(LikeStatus.DISLIKE);

        Long reviewId = 1l;
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        mockMvc.perform(post("/reviews/{review-id}/like", reviewId)
                        .header(AUTHORIZATION, jwtTokenProvider.issue(memberDetails))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(REVIEW_LIKE_UPDATE))

                .andDo(document("review/like/not_first",
                        getDocumentRequest(), getDocumentResponse(),

                        HeaderDocumentation.requestHeaders(
                                headerWithName(AUTHORIZATION).description("인증 코드")
                        ),

                        RequestDocumentation.pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),

                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }
}