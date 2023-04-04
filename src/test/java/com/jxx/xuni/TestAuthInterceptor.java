package com.jxx.xuni;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class TestAuthInterceptor extends AuthInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("TestAuthInterceptor");
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", new MemberDetails(1l, "leesin5498@naver.com", "이재헌"));
        return true;
    }
}
