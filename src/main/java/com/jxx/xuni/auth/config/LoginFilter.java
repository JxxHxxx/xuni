package com.jxx.xuni.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/auth/*", "/h2-console/*"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[Initialize LoginFilter Bean]");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isNotAuthenticationRequired(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("loginMember") == null) {
            throw new UnauthenticatedException("로그인이 필요한 서비스입니다.");
        }

        chain.doFilter(request, response);
    }

    private static boolean isNotAuthenticationRequired(HttpServletRequest httpRequest) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, httpRequest.getRequestURI());
    }
}
