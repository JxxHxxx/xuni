package com.xuni.core.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    public Member(LoginInfo loginInfo, String name, AuthProvider authProvider) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.authProvider = authProvider;
        this.authority = Authority.USER;
    }

    @Builder
    protected Member(Long id, LoginInfo loginInfo, String name) {
        this.id = id;
        this.loginInfo = loginInfo;
        this.name = name;
    }

    public void matches(String password) {
        if (loginInfo.isNotSame(password)) throw new PasswordNotMatchedException(AuthResponseMessage.LOGIN_FAIL);
    }

    public String receiveEmail() {
        return loginInfo.getEmail();
    }

    public boolean isAuthProvider(AuthProvider authProvider) {
        return this.authProvider.equals(authProvider);
    }
}
