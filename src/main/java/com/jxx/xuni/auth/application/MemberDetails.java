package com.jxx.xuni.auth.application;

import com.jxx.xuni.member.domain.Authority;

/**
 * 추후 확장성을 위해 인터페이스화
 */

public interface MemberDetails {
    String getName();
    String getEmail();
    Long getUserId();
    Authority getAuthority();
}
