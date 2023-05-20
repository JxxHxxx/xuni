package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.domain.Authority;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.common.exception.NotPermissionException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import java.lang.reflect.Method;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.ONLY_ADMIN;
import static com.jxx.xuni.common.exception.CommonExceptionMessage.REQUIRED_LOGIN;

@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method method = getMethod(handler);
        if (isNotAdmin(method)) {
            return true;
        }

        MemberDetails memberDetails = getMemberDetails(request);
        if (Authority.ADMIN.equals(memberDetails.getAuthority())) {
            return true;
        }
        throw new NotPermissionException(ONLY_ADMIN);
    }

    private boolean isNotAdmin(Method method) {
        return method.getDeclaredAnnotation(Admin.class) == null ? true : false;
    }

    private Method getMethod(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethod();
    }

    private MemberDetails getMemberDetails(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            jwtTokenManager.validateAccessToken(token);
            return jwtTokenManager.getMemberDetails(token);
        } catch (NullPointerException e) {
            throw new SecurityException(REQUIRED_LOGIN);
        }
    }
}
