package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.SimpleHostCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@TestConfiguration
public class GroupControllerTestConfig implements WebMvcConfigurer {
    @MockBean
    GroupRepository groupRepository;
    @MockBean
    SimpleHostCreator simpleHostCreator;

    @Bean
    GroupCreateService groupCreateService() {
        return new GroupCreateService(groupRepository, simpleHostCreator);
    }

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }
}
