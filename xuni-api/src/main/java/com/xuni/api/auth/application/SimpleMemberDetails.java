package com.xuni.api.auth.application;

import com.xuni.auth.domain.Authority;
import com.xuni.common.exception.CommonExceptionMessage;
import com.xuni.common.exception.NotPermissionException;
import lombok.Getter;

import static com.xuni.auth.domain.Authority.*;
import static com.xuni.common.exception.CommonExceptionMessage.*;

@Getter
public class SimpleMemberDetails implements MemberDetails {
    private Long userId;
    private String email;
    private String name;
    private Authority authority;

    public static final SimpleMemberDetails GUEST() {
        return new SimpleMemberDetails(Long.MAX_VALUE, null, "guest");
    }

    public SimpleMemberDetails(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.authority = USER;
    }

    public SimpleMemberDetails(Long userId, String email, String name, Authority authority) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.authority = authority;
    }

    @Override
    public void checkPrivateAuthority(Long userId) {
        if (this.userId != userId) {
            throw new NotPermissionException(PRIVATE_ACCESSIBLE);
        }
    }
}
