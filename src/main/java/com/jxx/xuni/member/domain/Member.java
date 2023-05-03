package com.jxx.xuni.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN_FAIL;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Embedded
    private LoginInfo loginInfo;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    private String name;
    private boolean isAuthenticated;

    @ElementCollection
    @CollectionTable(name = "member_auth_code", joinColumns = @JoinColumn(name = "member_id"))
    private List<AuthCode> authCode = new ArrayList<>();

    public Member(LoginInfo loginInfo, String name) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.authority = Authority.USER;
        this.isAuthenticated = false;
    }

    @Builder
    protected Member(Long id, LoginInfo loginInfo, String name) {
        this.id = id;
        this.loginInfo = loginInfo;
        this.name = name;
        this.isAuthenticated = true;
    }

    public void matches(String password) {
        if (loginInfo.isNotSame(password)) throw new PasswordNotMatchedException(LOGIN_FAIL);
    }

    public String receiveEmail() {
        return loginInfo.getEmail();
    }

    public void checkAuthenticated() {
        if (!this.isAuthenticated) throw new IllegalArgumentException("인증되지 않음");
    }

    public void createAuthCode(AuthCode authCode) {
        this.getAuthCode().add(authCode);
    }

    public void verifySignUpAuthCode(String code) {
        AuthCode signUpAuthCode = receiveAuthcode();
        matchesAuthCode(code, signUpAuthCode);

        this.isAuthenticated = true;
    }

    private AuthCode receiveAuthcode() {
        List<AuthCode> authCode = this.getAuthCode();
        return authCode.stream().filter(a -> UsageType.SIGNUP.equals(a.getUsageType())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }

    private void matchesAuthCode(String code, AuthCode signUpAuthCode) {
        if (isNotCorrectCode(code, signUpAuthCode)) throw new IllegalArgumentException("유효하지 않은 인증번호입니다.");
    }

    private boolean isNotCorrectCode(String code, AuthCode signUpAuthCode) {
        return !signUpAuthCode.getValue().equals(code);
    }

}
