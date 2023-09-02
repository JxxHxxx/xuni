package com.xuni.api.review.presentation;

import com.xuni.api.ApiDocumentUtils;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.api.auth.support.JwtTokenProvider;
import com.xuni.api.review.application.ReviewLikeService;
import com.xuni.api.support.ControllerCommon;
import com.xuni.api.support.JwtTestConfiguration;
import com.xuni.review.domain.LikeStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;


import static com.xuni.api.review.dto.response.ReviewApiMessage.REVIEW_LIKE_CREATE;
import static com.xuni.api.review.dto.response.ReviewApiMessage.REVIEW_LIKE_UPDATE;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({ReviewControllerTestConfig.class, JwtTestConfiguration.class})
class ReviewLikeControllerTest extends ControllerCommon {

    @Autowired
    JwtTokenProvider testJwtTokenProvider;
    @Autowired
    ReviewLikeService reviewLikeService;

    @Test
    void execute_docs_first_time() throws Exception {
        given(reviewLikeService.execute(anyLong(), anyLong())).willReturn(LikeStatus.INIT);

        Long reviewId = 1l;
        SimpleMemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@naver.com", "유니");
        mockMvc.perform(post("/reviews/{review-id}/like", reviewId)
                .header(HttpHeaders.AUTHORIZATION, testJwtTokenProvider.issue(memberDetails))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(REVIEW_LIKE_CREATE))

                .andDo(document("review/like/first",
                        ApiDocumentUtils.getDocumentRequest(), ApiDocumentUtils.getDocumentResponse(),

                        HeaderDocumentation.requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 코드")
                        ),

                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),

                        responseFields(
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
                        .header(HttpHeaders.AUTHORIZATION, testJwtTokenProvider.issue(memberDetails))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(REVIEW_LIKE_UPDATE))

                .andDo(document("review/like/not_first",
                        ApiDocumentUtils.getDocumentRequest(), ApiDocumentUtils.getDocumentResponse(),

                        HeaderDocumentation.requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 코드")
                        ),

                        pathParameters(
                                parameterWithName("review-id").description("리뷰 식별자")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }
}