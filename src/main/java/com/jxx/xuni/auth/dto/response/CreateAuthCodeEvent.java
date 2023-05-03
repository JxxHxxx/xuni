package com.jxx.xuni.auth.dto.response;

public record CreateAuthCodeEvent(
        String authCodeId,
        String email,
        String value
) {
}
