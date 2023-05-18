package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.support.JwtTokenManager;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.REQUIRED_LOGIN;

@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;
    private static final List<String> PRIVATE_ACCESS_URI_PREFIX = List.of("/members");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (notRequiredAuthentication(request)) {
            return true;
        }

        String token = extractAuthorizationHeader(request);
        jwtTokenManager.validateAccessToken(token);
        return true;
    }

    private boolean notRequiredAuthentication(HttpServletRequest request) {
        return isGetMethod(request) && isPublicURIPrefix(request);
    }

    private boolean isGetMethod(HttpServletRequest request) {
        return "GET".equals(request.getMethod());
    }

    private boolean isPublicURIPrefix(HttpServletRequest request) {
        for (String domain : PRIVATE_ACCESS_URI_PREFIX) {
            if (request.getRequestURI().startsWith(domain)) return false;
        }
        return true;
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        try {
            return request.getHeader("Authorization").substring(7);
        } catch (NullPointerException exception) {
            throw new SecurityException(REQUIRED_LOGIN);
        }
    }
}
