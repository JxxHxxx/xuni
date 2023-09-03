package com.xuni.api.auth.application;

import com.xuni.core.auth.domain.Authority;

/**
 * 추후 확장성을 위해 인터페이스화
 */

public interface MemberDetails {
    String getName();
    String getEmail();
    Long getUserId();
    Authority getAuthority();

    void checkPrivateAuthority(Long userId);
}
