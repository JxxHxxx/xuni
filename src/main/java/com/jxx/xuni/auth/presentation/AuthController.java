package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.AuthMailService;
import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.request.AuthCodeForm;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import com.jxx.xuni.auth.dto.response.LoginResponse;
import com.jxx.xuni.auth.dto.response.AuthSimpleResult;
import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.member.domain.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMailService authMailService;

    @PostMapping("/auth/signup-email")
    public ResponseEntity<AuthSimpleResult> signupForEmail(@RequestBody @Validated SignupForm signupForm) {
        Member member = authService.signup(signupForm);
        authMailService.sendAuthCode(member.receiveEmail());
        return new ResponseEntity(AuthSimpleResult.signup(), CREATED);
    }

    @PatchMapping("/auth/verify-code")
    public ResponseEntity<AuthSimpleResult> verifyAuthCode(@RequestBody AuthCodeForm authCodeForm) {
        authMailService.verifyAuthCode(authCodeForm);

        return ResponseEntity.ok(AuthSimpleResult.verified());
    }
    @PostMapping("/auth/login")
    public ResponseEntity<AuthSimpleResult> login(@RequestBody LoginForm loginForm, HttpServletResponse response) {
        MemberDetails memberDetails = authService.login(loginForm);
        response.addHeader("Authorization", jwtTokenProvider.issue(memberDetails));
        return ResponseEntity.ok(AuthSimpleResult.login(LoginResponse.from(memberDetails)));
    }
}
