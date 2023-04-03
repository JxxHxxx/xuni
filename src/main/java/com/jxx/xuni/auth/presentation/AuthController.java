package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.AuthService;
import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import com.jxx.xuni.auth.dto.response.LoginResponse;
import com.jxx.xuni.auth.dto.response.AuthSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final HttpSession httpSession;

    @PostMapping("/auth/signup-email")
    public ResponseEntity<AuthSimpleResult> SignupForEmail(@RequestBody @Validated SignupForm signupForm) {
        authService.signup(signupForm);
        return new ResponseEntity(AuthSimpleResult.signup(), CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthSimpleResult> login(@RequestBody LoginForm loginForm) {
        MemberDetails memberDetails = authService.login(loginForm);
        httpSession.setAttribute("loginMember", memberDetails);
        return ResponseEntity.ok(AuthSimpleResult.login(LoginResponse.from(memberDetails)));
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<AuthSimpleResult> logout() {
        httpSession.removeAttribute("loginMember");
        return ResponseEntity.ok(AuthSimpleResult.logout());
    }
}
