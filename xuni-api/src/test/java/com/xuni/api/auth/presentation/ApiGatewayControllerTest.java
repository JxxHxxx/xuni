package com.xuni.api.auth.presentation;

import com.xuni.api.ApiDocumentUtils;
import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.api.support.ControllerCommon;
import com.xuni.api.support.JwtTestConfiguration;
import com.xuni.core.auth.domain.Authority;
import com.xuni.api.auth.support.JwtTokenManager;
import com.xuni.api.auth.support.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static com.xuni.api.auth.dto.response.AuthResponseMessage.READ_MEMBER_DETAILS;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({AuthControllerTestConfig.class, JwtTestConfiguration.class})
class ApiGatewayControllerTest extends ControllerCommon {

    @Autowired
    JwtTokenProvider testJwtTokenProvider;
    @Autowired
    JwtTokenManager testJwtTokenManager;
    String token;

    @BeforeEach
    void beforeEach() {
        // initialize token
        MemberDetails memberDetails = new SimpleMemberDetails(1l, "xuni@xuni.com", "uni", Authority.ADMIN);
        token = testJwtTokenProvider.issue(memberDetails);
    }

    @Test
    void receive_member_details_docs() throws Exception {
        mockMvc.perform(get("/api/auth")
                .header(AUTHORIZATION, token))

                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value(READ_MEMBER_DETAILS))

                .andDo(MockMvcRestDocumentation.document("auth/receive-member-detail",
                        ApiDocumentUtils.getDocumentRequest(), ApiDocumentUtils.getDocumentResponse(),
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