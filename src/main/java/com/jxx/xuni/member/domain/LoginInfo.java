package com.jxx.xuni.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginInfo {
    private String email;
    private String password;

    private LoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginInfo of(String email, String password) {
        return new LoginInfo(email, password);
    }

    protected boolean isNotSame(String password) {
        return !this.password.equals(password);
    }
}
