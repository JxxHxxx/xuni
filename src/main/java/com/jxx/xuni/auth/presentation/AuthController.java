package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.AuthMailService;
import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.request.*;
import com.jxx.xuni.auth.dto.response.*;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMailService authMailService;

    @PostMapping("/auth/codes")
    public ResponseEntity<AuthResult> createAndSendAuthCode(@RequestBody EmailForm form) {
        authService.checkEmailAndAuthProvider(form);
        CreateAuthCodeEvent event = authService.createAuthCode(form);
        authMailService.sendAuthCode(event);

        AuthCodeSimpleResponse response = new AuthCodeSimpleResponse(event.authCodeId());

        return new ResponseEntity<>(new AuthResult<>(201, SEND_AUTH_CODE, response), CREATED);
    }

    @PatchMapping("/auth/codes")
    public ResponseEntity<AuthResult> verifyAuthCode(@RequestBody AuthCodeForm form) {
        VerifyAuthCodeEvent event = authService.verifyAuthCode(form);
        AuthCodeSimpleResponse response = new AuthCodeSimpleResponse(event.authCodeId());
        return ResponseEntity.ok(new AuthResult<>(200, AUTHENTICATE_CODE, response));
    }

    @PostMapping("/auth/signup-email")
    public ResponseEntity<AuthSimpleResult> signupForEmail(@RequestBody @Validated SignupForm signupForm) {
        authService.signup(signupForm);
        return new ResponseEntity(AuthSimpleResult.signup(), CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResult> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        MemberDetails memberDetails = authService.login(loginForm);
        response.addHeader("Authorization", jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(new AuthResult<>(200, LOGIN, LoginResponse.from(memberDetails)));
    }
}
