package com.xuni.api.auth.presentation;

import com.xuni.api.auth.application.AuthService;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.api.auth.dto.request.AuthCodeForm;
import com.xuni.api.auth.dto.request.EmailForm;
import com.xuni.api.auth.dto.request.LoginForm;
import com.xuni.api.auth.dto.request.SignupForm;
import com.xuni.api.auth.dto.response.CreateAuthCodeEvent;
import com.xuni.api.auth.dto.response.VerifyAuthCodeEvent;
import com.xuni.api.support.ControllerCommon;
import com.xuni.api.support.JwtTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.xuni.api.auth.dto.response.AuthResponseMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({AuthControllerTestConfig.class, JwtTestConfiguration.class})
class AuthControllerTest extends ControllerCommon {

    @Autowired
    AuthService authService;

    @DisplayName("인증 코드 생성 API")
    @Test
    void create_and_send_auth_code_docs() throws Exception {
        EmailForm form = new EmailForm("leesin5498@xuni.com");

        CreateAuthCodeEvent event = new CreateAuthCodeEvent(
                "a0f89f19-0b63-49b3-9f57-c3c7b7cf78fa",
                "leesin5498@xuni.com",
                "123456789012");
        BDDMockito.given(authService.createAuthCode(form)).willReturn(event);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)));

        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(SEND_AUTH_CODE))

                .andDo(MockMvcRestDocumentation.document("auth/create-code",

                        requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING).description("가입할 이메일")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 본문"),
                                fieldWithPath("response.authCodeId").type(JsonFieldType.STRING).description("인증 코드 식별자")
                        )
                ));
    }

    @DisplayName("인증 코드 값 검증 API")
    @Test
    void verify_auth_code_docs() throws Exception {
        AuthCodeForm form = new AuthCodeForm("a0f89f19-0b63-49b3-9f57-c3c7b7cf78fa", "123456789012");

        VerifyAuthCodeEvent event = new VerifyAuthCodeEvent("a0f89f19-0b63-49b3-9f57-c3c7b7cf78fa");
        BDDMockito.given(authService.verifyAuthCode(form)).willReturn(event);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/auth/codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AUTHENTICATE_CODE))

                .andDo(MockMvcRestDocumentation.document("auth/verify-code",
                        requestFields(
                                fieldWithPath("authCodeId").type(JsonFieldType.STRING).description("인증 코드 식별자"),
                                fieldWithPath("value").type(JsonFieldType.STRING).description("인증 코드 값")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 본문"),
                                fieldWithPath("response.authCodeId").type(JsonFieldType.STRING).description("인증 코드 식별자")
                        )
                ));
    }

    @DisplayName("이메일 회원 가입 API")
    @Test
    void signup_for_email_docs() throws Exception {
        SignupForm form = new SignupForm("a0f89f19-0b63-49b3-9f57-c3c7b7cf78fa", "0000", "김유니");

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/signup-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)));

        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(SIGNUP))

                .andDo(MockMvcRestDocumentation.document("auth/signup",
                        requestFields(
                                fieldWithPath("authCodeId").type(JsonFieldType.STRING).description("인증 코드 식별자"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                    ));
    }

    class MockMemberDetails extends SimpleMemberDetails {

        private String mockField;

        public MockMemberDetails(Long userId, String email, String name, String mockField) {
            super(userId, email, name);
            this.mockField = mockField;
        }
    }

    @DisplayName("로그인 API")
    @Test
    void login_docs() throws Exception {
        LoginForm form = new LoginForm("leesin5498@xuni.com", "0000");
        MockMemberDetails mockMemberDetails = new MockMemberDetails(1l, "leesin5498@xuni.com", "김유니", any());
        BDDMockito.given(authService.login(form)).willReturn(mockMemberDetails);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(LOGIN))

                .andDo(MockMvcRestDocumentation.document("auth/login",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("인증 코드 식별자"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 본문"),
                                fieldWithPath("response.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("response.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("response.userId").type(JsonFieldType.NUMBER).description("유저 식별자")
                        ),

                        HeaderDocumentation.responseHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                        )
                ));
    }
}