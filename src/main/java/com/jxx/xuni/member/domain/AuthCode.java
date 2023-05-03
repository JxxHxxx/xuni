package com.jxx.xuni.member.domain;

import com.jxx.xuni.member.domain.exception.AuthCodeException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.jxx.xuni.member.domain.exception.ExceptionMessage.INCORRECT_AUTH_CODE_VALUE;
import static com.jxx.xuni.member.domain.exception.ExceptionMessage.UNAUTHENTICATED;

@Getter
@Entity
@Table(name = "member_auth_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {

    @Id
    private String authCodeId;
    private String email;
    private String value;
    @Enumerated(EnumType.STRING)
    private UsageType usageType;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private boolean isAuthenticated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public AuthCode(String email, UsageType usageType) {
        this.authCodeId = UUID.randomUUID().toString();
        this.value = generateValue();
        this.email = email;
        this.usageType = usageType;
        this.createdTime = LocalDateTime.now();
        this.modifiedTime = LocalDateTime.now();
        this.isAuthenticated = false;
    }

    public static String generateValue() {
        Long randomValue = ThreadLocalRandom.current().nextLong(100000000000l, 1000000000000l);
        return randomValue.toString();
    }

    public void verifyAuthCode(String value) {
        checkAuthCodeOf(value);
        authenticate();
    }

    public Member createMember(String email, String password, String name) {
        checkAuthenticated();
        return new Member(LoginInfo.of(email, password), name);
    }

    public void setMember(Member member) {
        this.member = member;
    }

    private void checkAuthenticated() {
        if (this.isAuthenticated == false) throw new AuthCodeException(UNAUTHENTICATED);
    }

    protected void authenticate() {
        this.isAuthenticated = true;
    }

    private void checkAuthCodeOf(String value) {
        if (!this.value.equals(value)) throw new AuthCodeException(INCORRECT_AUTH_CODE_VALUE);
    }
}
