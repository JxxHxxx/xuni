package com.jxx.xuni.auth.config;

import com.jxx.xuni.auth.presentation.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class AuthConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("[Register AuthInterceptor]");
        registry.addInterceptor(new AuthInterceptor())
                .excludePathPatterns("/auth/**")
                .excludePathPatterns("/h2-console/**")
                .order(2);
    }
}