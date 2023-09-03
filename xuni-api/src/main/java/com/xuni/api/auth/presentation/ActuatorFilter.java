package com.xuni.api.auth.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.support.JwtTokenManager;
import com.xuni.core.common.exception.NotPermissionException;
import com.xuni.core.auth.domain.Authority;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ActuatorFilter implements Filter {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (isActuatorEndpoint(httpRequest)) {
            checkAdminAuthority(httpRequest);
            chain.doFilter(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void checkAdminAuthority(HttpServletRequest request) {
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(request.getHeader(HttpHeaders.AUTHORIZATION));

        if (!Authority.ADMIN.equals(memberDetails.getAuthority())) {
            throw new NotPermissionException("접근 권한이 존재하지 않습니다.");
        }

    }

    private static boolean isActuatorEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }
}
