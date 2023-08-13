package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.support.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        String token = jwtTokenManager.removeBearerFrom(request.getHeader(AUTHORIZATION));
        jwtTokenManager.validate(token);
        return true;
    }

    private boolean notRequiredAuthentication(HttpServletRequest request) {
        return GET.name().equals(request.getMethod());
    }

    private boolean isPreflight(HttpServletRequest request) {
        return OPTIONS.name().equals(request.getMethod());
    }

}
