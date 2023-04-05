package com.jxx.xuni.auth.config;

import com.jxx.xuni.auth.presentation.JwtAuthInterceptor;
import com.jxx.xuni.auth.support.JwtTokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Profile("default")
@Configuration
public class AuthConfig implements WebMvcConfigurer{

    private final JwtTokenManager jwtTokenManager;

    public AuthConfig(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("[Register JwtAuthInterceptor]");
        registry.addInterceptor(new JwtAuthInterceptor(jwtTokenManager))
                .excludePathPatterns("/auth/**")
                .excludePathPatterns("/h2-console/**")
                .order(2);
    }

}