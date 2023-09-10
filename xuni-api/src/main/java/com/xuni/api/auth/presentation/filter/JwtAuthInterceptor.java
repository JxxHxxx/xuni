package com.xuni.api.auth.presentation.filter;

import com.xuni.api.auth.application.jwt.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (notRequiredAuthentication(request) || isPreflight(request)) {
            return true;
        }
        String token = jwtTokenManager.removeBearerFrom(request.getHeader(HttpHeaders.AUTHORIZATION));
        jwtTokenManager.validate(token);
        return true;
    }

    private boolean notRequiredAuthentication(HttpServletRequest request) {
        return HttpMethod.GET.name().equals(request.getMethod());
    }

    private boolean isPreflight(HttpServletRequest request) {
        return HttpMethod.OPTIONS.name().equals(request.getMethod());
    }

}
