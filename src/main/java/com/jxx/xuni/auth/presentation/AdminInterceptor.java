package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.common.exception.NotPermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.lang.reflect.Method;

import static com.jxx.xuni.auth.domain.Authority.*;
import static com.jxx.xuni.common.exception.CommonExceptionMessage.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (OPTIONS.name().equals(request.getMethod())) {
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
        throw new NotPermissionException(ONLY_ADMIN);
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
            return jwtTokenManager.getMemberDetails(request.getHeader(AUTHORIZATION));

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Authorization 헤더 " + EMPTY_VALUE);
        }
    }
}
