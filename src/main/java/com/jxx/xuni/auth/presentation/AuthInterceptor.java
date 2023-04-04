package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.config.UnauthenticatedException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            throw new UnauthenticatedException("로그인이 필요한 서비스입니다.");
        }
        return true;
    }

}
