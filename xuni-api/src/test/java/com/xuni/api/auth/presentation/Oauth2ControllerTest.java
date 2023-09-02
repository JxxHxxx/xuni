package com.xuni.api.auth.presentation;


import com.xuni.api.auth.application.GoogleClient;
import com.xuni.api.support.ControllerCommon;
import com.xuni.api.support.JwtTestConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

@Import({AuthControllerTestConfig.class, JwtTestConfiguration.class})
class Oauth2ControllerTest extends ControllerCommon {

    @Autowired
    GoogleClient googleClient;

    @Test
    @Disabled("원인을 파악할 수 없는 배포 환경에서의 오류")
    void google_login() throws Exception {
        ResultActions result = mockMvc.perform(post("/auth/login/google"));

        result
                .andExpect(MockMvcResultMatchers.status().is(302))

                .andDo(document("/auth/oauth2/google"));
    }
}