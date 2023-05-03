package com.jxx.xuni.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupForm {
    private String authCodeId;
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]{4,12}$")
    private String password;
    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,8}$")
    private String name;
}
