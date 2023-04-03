package com.jxx.xuni.auth.dto.request;

import lombok.Getter;

@Getter
public class SignupForm {
    private String email;
    private String password;
    private String name;
}
