package com.xuni.api.auth.presentation.controller;

import com.xuni.api.auth.application.GoogleClient;
import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.dto.response.LoginResponse;
import com.xuni.api.auth.application.jwt.JwtTokenProvider;
import com.xuni.core.common.http.DataResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.xuni.api.auth.dto.response.AuthResponseMessage.LOGIN;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleClient googleClient;

    @PostMapping("/auth/login/google")
    public void loginUrlGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleClient.getAuthCodeUrl());
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<DataResponse> login(@RequestParam("code") String accessCode, HttpServletResponse response) {
        MemberDetails memberDetails = googleClient.login(accessCode);
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(new DataResponse(200, LOGIN, LoginResponse.from(memberDetails)));
    }
}
