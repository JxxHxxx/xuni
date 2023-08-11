package com.jxx.xuni.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jxx.xuni.auth.presentation.gateway.ApiGatewayController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({AuthController.class, Oauth2Controller.class, ApiGatewayController.class})
@AutoConfigureRestDocs
public class AuthCommon {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
