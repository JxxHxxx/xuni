package com.xuni.api.auth.presentation.filter;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.annotation.Admin;
import com.xuni.api.auth.application.jwt.JwtTokenManager;
import com.xuni.core.common.exception.CommonExceptionMessage;
import com.xuni.core.common.exception.NotPermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.lang.reflect.Method;

import static com.xuni.core.auth.domain.Authority.*;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        Method method = getMethod(handler);
        if (isNotAdmin(method)) {
            return true;
        }

        MemberDetails memberDetails = getMemberDetails(request);
        if (ADMIN.equals(memberDetails.getAuthority())) {
            return true;
        }
        throw new NotPermissionException(CommonExceptionMessage.ONLY_ADMIN);
    }

    private Method getMethod(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethod();
    }

    private boolean isNotAdmin(Method method) {
        return method.getDeclaredAnnotation(Admin.class) == null ? true : false;
    }

    private MemberDetails getMemberDetails(HttpServletRequest request) {
        try {
            return jwtTokenManager.getMemberDetails(request.getHeader(HttpHeaders.AUTHORIZATION));

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Authorization 헤더 " + CommonExceptionMessage.EMPTY_VALUE);
        }
    }
}
