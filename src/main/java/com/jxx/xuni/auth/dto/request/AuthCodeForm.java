package com.jxx.xuni.auth.dto.request;

public record AuthCodeForm(
        Long memberId,
        String code
) {
}
