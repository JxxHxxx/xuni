package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.support.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.EMPTY_VALUE;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (notRequiredAuthentication(request) || isPreflight(request)) {
            return true;
        }

        String token = extractAuthorizationHeader(request);
        jwtTokenManager.validateAccessToken(token);
        return true;
    }

    private boolean notRequiredAuthentication(HttpServletRequest request) {
        return GET.name().equals(request.getMethod());
    }

    private boolean isPreflight(HttpServletRequest request) {
        return OPTIONS.name().equals(request.getMethod());
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        try {
            return jwtTokenManager.extractTokenFromBearer(request.getHeader(AUTHORIZATION));
        } catch (NullPointerException exception) {
            throw new IllegalArgumentException("Authorization 헤더 " + EMPTY_VALUE);
        }
    }
}
