package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.auth.config.UnauthenticatedException;
import com.jxx.xuni.auth.support.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.REQUIRED_LOGIN;

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
            String token = request.getHeader("Authorization");
            return jwtTokenManager.getMemberDetails(token.substring(7));
        } catch (NullPointerException exception) {
            return SimpleMemberDetails.GUEST();
        } catch (StringIndexOutOfBoundsException exception) {
            throw new UnauthenticatedException(REQUIRED_LOGIN);
        }
    }
}
