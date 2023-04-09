package com.jxx.xuni.auth.application;

import com.jxx.xuni.member.domain.Authority;
import lombok.Getter;

import static com.jxx.xuni.member.domain.Authority.*;

@Getter
public class SimpleMemberDetails implements MemberDetails {
    private Long userId;
    private String email;
    private String name;
    private Authority authority;

    public SimpleMemberDetails(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.authority = USER;
    }

    public SimpleMemberDetails(Long userId, String email, String name, Authority authority) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.authority = authority;
    }
}
