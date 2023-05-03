package com.jxx.xuni.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {

    private String value;
    @Enumerated(EnumType.STRING)
    private UsageType usageType;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public AuthCode(String value, UsageType usageType) {
        this.value = value;
        this.usageType = usageType;
        this.createdTime = LocalDateTime.now();
        this.modifiedTime = LocalDateTime.now();
    }

    public static String generateValue() {
        Long randomValue = ThreadLocalRandom.current().nextLong(100000000000l, 1000000000000l);
        return randomValue.toString();
    }
}
