package com.jxx.xuni.auth.dto.response;

import com.jxx.xuni.auth.application.MemberDetails;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String email;
    private final String name;
    private final Long userId;

    public LoginResponse(String email, String name, Long userId) {
        this.email = email;
        this.name = name;
        this.userId = userId;
    }

    public static LoginResponse from(MemberDetails memberDetails) {
        return new LoginResponse(memberDetails.getEmail(), memberDetails.getName(), memberDetails.getUserId());
    }
}
