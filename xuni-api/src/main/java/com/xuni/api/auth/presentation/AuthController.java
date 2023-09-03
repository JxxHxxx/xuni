package com.xuni.api.auth.presentation;

import com.xuni.api.auth.application.AuthMailService;
import com.xuni.api.auth.application.AuthService;
import com.xuni.api.auth.dto.request.AuthCodeForm;
import com.xuni.api.auth.dto.request.EmailForm;
import com.xuni.api.auth.dto.request.LoginForm;
import com.xuni.api.auth.dto.request.SignupForm;
import com.xuni.api.auth.support.JwtTokenProvider;
import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.dto.response.*;
import com.xuni.core.common.http.DataResponse;
import com.xuni.core.common.http.SimpleResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.xuni.api.auth.dto.response.AuthResponseMessage.*;

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
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(new DataResponse(200, LOGIN, LoginResponse.from(memberDetails)));
    }
}
