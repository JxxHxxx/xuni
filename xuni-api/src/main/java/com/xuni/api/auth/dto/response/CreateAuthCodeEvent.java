package com.xuni.api.auth.dto.response;

public record CreateAuthCodeEvent(
        String authCodeId,
        String email,
        String value
) {
}
