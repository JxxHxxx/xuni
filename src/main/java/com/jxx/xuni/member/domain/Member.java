package com.jxx.xuni.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.jxx.xuni.member.dto.response.MemberResponseMessage.LOGIN_FAIL;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private LoginInfo loginInfo;

    private String name;

    public Member(LoginInfo loginInfo, String name) {
        this.loginInfo = loginInfo;
        this.name = name;
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
}
