package com.jxx.xuni;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.config.LoginFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class TestLoginFilter extends LoginFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        session.setAttribute("loginMember", new MemberDetails(1l, "leesin5498@naver.com", "이재헌"));

        chain.doFilter(request,response);
    }
}
