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
    // FE 단에서 인증 코드 ID를 넣어줘야함
    @NotBlank(message = "알 수 없는 에러가 발생했습니다.")
    private String authCodeId;
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]{4,12}$", message = "비밀번호는 4 ~ 12자 사이의 소문자, 대문자, 숫자로 이루어져야 합니다.")
    private String password;
    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,8}$", message = "이름은 2 ~ 8자 사이의 한글이어야 합니다.")
    private String name;
}
