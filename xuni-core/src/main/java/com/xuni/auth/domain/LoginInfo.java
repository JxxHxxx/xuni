package com.xuni.auth.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    public String getEmail() {
        return email;
    }
}
