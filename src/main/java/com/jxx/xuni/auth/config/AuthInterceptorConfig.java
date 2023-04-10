package com.jxx.xuni.auth.config;

import com.jxx.xuni.auth.presentation.ActuatorFilter;
import com.jxx.xuni.auth.presentation.JwtAuthInterceptor;
import com.jxx.xuni.auth.presentation.AuthenticatedMemberArgumentResolver;
import com.jxx.xuni.auth.support.JwtTokenManager;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Profile("default")
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer{

    private final JwtTokenManager jwtTokenManager;

    public AuthInterceptorConfig(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Bean
    public FilterRegistrationBean actuatorFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new ActuatorFilter(jwtTokenManager));
        return filterFilterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("[Register JwtAuthInterceptor]");
        registry.addInterceptor(new JwtAuthInterceptor(jwtTokenManager))
                .excludePathPatterns("/auth/**")
                .excludePathPatterns("/h2-console/**")
                .order(2);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticatedMemberArgumentResolver(jwtTokenManager));
    }
}