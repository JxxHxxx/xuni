package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthenticatedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportParameter 실행");

        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(AuthenticatedMember.class);
        boolean hasMemberDetailsType = MemberDetails.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginMemberAnnotation && hasMemberDetailsType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = request.getHeader("Authorization");
        String extractedToken = token.substring(7);

        return jwtTokenManager.getMemberDetails(extractedToken);
    }
}
