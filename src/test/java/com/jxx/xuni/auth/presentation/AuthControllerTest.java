package com.jxx.xuni.auth.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jxx.xuni.auth.application.AuthControllerTestConfig;
import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.dto.request.AuthCodeForm;
import com.jxx.xuni.auth.dto.request.EmailForm;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import com.jxx.xuni.auth.dto.response.CreateAuthCodeEvent;
import com.jxx.xuni.auth.dto.response.VerifyAuthCodeEvent;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.ResultActions;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(AuthControllerTestConfig.class)
class AuthControllerTest extends AuthCommon{

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

    @DisplayName("로그인 API")
    @Test
    void login_docs() throws Exception {
        LoginForm form = new LoginForm("leesin5498@xuni.com", "0000");

        MemberDetails memberDetails = new SimpleMemberDetails(any(), "leesin54982naver.com", "김유니");
        BDDMockito.given(authService.login(form)).willReturn(memberDetails);

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
                                fieldWithPath("response.name").type(JsonFieldType.STRING).description("이름")
                        ),

                        HeaderDocumentation.responseHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                        )
                ));
    }
}