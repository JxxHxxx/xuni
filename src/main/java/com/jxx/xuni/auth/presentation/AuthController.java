package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.AuthMailService;
import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.request.*;
import com.jxx.xuni.auth.dto.response.*;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.common.http.DataResponse;
import com.jxx.xuni.common.http.SimpleResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.*;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMailService authMailService;

    @PostMapping("/auth/codes")
    public ResponseEntity<DataResponse> createAndSendAuthCode(@RequestBody EmailForm form) {
        authService.checkEmailAndAuthProvider(form);
        CreateAuthCodeEvent event = authService.createAuthCode(form);
        authMailService.sendAuthCode(event);

        AuthCodeSimpleResponse response = new AuthCodeSimpleResponse(event.authCodeId());

        return ResponseEntity.status(201).body(new DataResponse(201, SEND_AUTH_CODE, response));
    }

    @PatchMapping("/auth/codes")
    public ResponseEntity<DataResponse> verifyAuthCode(@RequestBody AuthCodeForm form) {
        VerifyAuthCodeEvent event = authService.verifyAuthCode(form);
        AuthCodeSimpleResponse response = new AuthCodeSimpleResponse(event.authCodeId());
        return ResponseEntity.ok(new DataResponse(200, AUTHENTICATE_CODE, response));
    }

    @PostMapping("/auth/signup-email")
    public ResponseEntity<SimpleResponse> signupForEmail(@RequestBody @Validated SignupForm signupForm) {
        authService.signup(signupForm);
        return ResponseEntity.status(201).body(SimpleResponse.create(SIGNUP));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<DataResponse> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        MemberDetails memberDetails = authService.login(loginForm);
        response.addHeader(AUTHORIZATION, jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(new DataResponse(200, LOGIN, LoginResponse.from(memberDetails)));
    }
}
