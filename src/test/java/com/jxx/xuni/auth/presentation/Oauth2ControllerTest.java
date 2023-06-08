package com.jxx.xuni.auth.presentation;


import com.jxx.xuni.auth.application.AuthControllerTestConfig;
import com.jxx.xuni.auth.application.GoogleClient;
import com.jxx.xuni.auth.application.PasswordEncoder;
import com.jxx.xuni.auth.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

@Import(AuthControllerTestConfig.class)
class Oauth2ControllerTest extends AuthCommon {

    @Autowired
    GoogleClient googleClient;

    @Test
    void google_login() throws Exception {
        ResultActions result = mockMvc.perform(post("/auth/login/google"));

        result
                .andExpect(MockMvcResultMatchers.status().is(302))

                .andDo(document("/auth/oauth2/google"));
    }
}