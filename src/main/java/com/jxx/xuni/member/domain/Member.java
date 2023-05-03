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

    public Member(LoginInfo loginInfo, String name) {
        this.loginInfo = loginInfo;
        this.name = name;
        this.authority = Authority.USER;
    }

    @Builder
    protected Member(Long id, LoginInfo loginInfo, String name) {
        this.id = id;
        this.loginInfo = loginInfo;
        this.name = name;
    }

    public void matches(String password) {
        if (loginInfo.isNotSame(password)) throw new PasswordNotMatchedException(LOGIN_FAIL);
    }

    public String receiveEmail() {
        return loginInfo.getEmail();
    }
}
