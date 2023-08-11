package com.jxx.xuni.auth.presentation.gateway;

import com.jxx.xuni.auth.application.AuthControllerTestConfig;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.domain.Authority;
import com.jxx.xuni.auth.dto.response.AuthResponseMessage;
import com.jxx.xuni.auth.presentation.AuthCommon;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.common.http.DataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.READ_MEMBER_DETAILS;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(AuthControllerTestConfig.class)
class ApiGatewayControllerTest extends AuthCommon {

    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    JwtTokenManager tokenManager;
    String token;

    @BeforeEach
    void beforeEach() {
        // initialize token
        MemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@xuni.com", "uni", Authority.ADMIN);
        token = tokenProvider.issue(memberDetails);
    }

    @Test
    void receive_member_details_docs() throws Exception {
        mockMvc.perform(get("/api/auth")
                .header(AUTHORIZATION, token))

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value(READ_MEMBER_DETAILS))

                .andDo(MockMvcRestDocumentation.document("auth/receive-member-detail",
                        getDocumentRequest(), getDocumentResponse(),
                        HeaderDocumentation.requestHeaders(
                                HeaderDocumentation.headerWithName(AUTHORIZATION).description("인증 코드")
                        ),

                        PayloadDocumentation.responseFields(
                                fieldWithPath("status").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("응답 메시지"),

                                fieldWithPath("response").type(OBJECT).description("조회 데이터"),
                                fieldWithPath("response.name").type(STRING).description("사용자 이름"),
                                fieldWithPath("response.email").type(STRING).description("사용자 계정"),
                                fieldWithPath("response.userId").type(NUMBER).description("사용자 식별자"),
                                fieldWithPath("response.authority").type(STRING).description("사용자 권한")
                        )
                ));

    }
}