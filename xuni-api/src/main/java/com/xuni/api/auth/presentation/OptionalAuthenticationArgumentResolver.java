package com.xuni.api.auth.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.application.SimpleMemberDetails;
import com.xuni.core.auth.domain.exception.UnauthenticatedException;
import com.xuni.api.auth.support.JwtTokenManager;
import com.xuni.core.common.exception.CommonExceptionMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class OptionalAuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(OptionalAuthentication.class);
        boolean hasMemberDetailsType = MemberDetails.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginMemberAnnotation && hasMemberDetailsType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        try {
            return jwtTokenManager.getMemberDetails(request.getHeader(HttpHeaders.AUTHORIZATION));
        } catch (NullPointerException exception) {
            return SimpleMemberDetails.GUEST();
        } catch (StringIndexOutOfBoundsException exception) {
            throw new UnauthenticatedException(CommonExceptionMessage.REQUIRED_LOGIN);
        }
    }
}
