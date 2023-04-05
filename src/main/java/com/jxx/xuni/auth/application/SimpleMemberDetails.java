package com.jxx.xuni.auth.application;

import lombok.Getter;

@Getter
public class SimpleMemberDetails implements MemberDetails {
    private Long userId;
    private String email;
    private String name;

    public SimpleMemberDetails(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
