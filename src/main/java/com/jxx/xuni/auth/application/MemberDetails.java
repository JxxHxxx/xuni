package com.jxx.xuni.auth.application;

import lombok.Getter;

@Getter
public class MemberDetails {
    private Long userId;
    private String email;
    private String name;

    public MemberDetails(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
