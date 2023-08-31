package com.xuni.api.auth.dto.request;

public record AuthCodeForm(
        String authCodeId,
        String value
) {
}
