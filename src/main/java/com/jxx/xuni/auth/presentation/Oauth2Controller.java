package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.GoogleClient;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.response.AuthResult;
import com.jxx.xuni.auth.dto.response.LoginResponse;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN;
import static org.apache.http.HttpHeaders.*;

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
    public ResponseEntity<AuthResult> login(@RequestParam("code") String accessCode, HttpServletResponse response) {
        MemberDetails memberDetails = googleClient.login(accessCode);
        response.addHeader(AUTHORIZATION, jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(new AuthResult<>(200, LOGIN, LoginResponse.from(memberDetails)));
    }
}
