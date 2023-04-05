package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import com.jxx.xuni.auth.dto.response.LoginResponse;
import com.jxx.xuni.auth.dto.response.AuthSimpleResult;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/signup-email")
    public ResponseEntity<AuthSimpleResult> SignupForEmail(@RequestBody @Validated SignupForm signupForm) {
        authService.signup(signupForm);
        return new ResponseEntity(AuthSimpleResult.signup(), CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthSimpleResult> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        MemberDetails memberDetails = authService.login(loginForm);
        response.addHeader("Authorization", jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(AuthSimpleResult.login(LoginResponse.from(memberDetails)));
    }
}
