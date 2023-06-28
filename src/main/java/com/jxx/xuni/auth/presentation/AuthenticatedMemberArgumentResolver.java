package com.jxx.xuni.auth.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.domain.exception.UnauthenticatedException;
import com.jxx.xuni.auth.support.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.REQUIRED_LOGIN;

@Slf4j
@RequiredArgsConstructor
public class AuthenticatedMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginMemberAnnotation = parameter.hasParameterAnnotation(AuthenticatedMember.class);
        boolean hasMemberDetailsType = MemberDetails.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginMemberAnnotation && hasMemberDetailsType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        try {
            String token = request.getHeader("Authorization");
            String extractedToken = token.substring(7);

            return jwtTokenManager.getMemberDetails(extractedToken);
        } catch (NullPointerException exception) {
            throw new UnauthenticatedException(REQUIRED_LOGIN);
        } catch (StringIndexOutOfBoundsException exception) {
            throw new UnauthenticatedException(REQUIRED_LOGIN);
        }
//        catch (ExpiredJwtException exception) {
//            throw new UnauthenticatedException(REQUIRED_LOGIN);
//        }
    }
}
