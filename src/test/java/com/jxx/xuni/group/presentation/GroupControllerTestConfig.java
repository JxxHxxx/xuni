package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.support.JwtTokenProvider;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.application.GroupJoinFacade;
import com.jxx.xuni.group.application.GroupManagingService;
import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.SimpleHostCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@TestConfiguration
public class GroupControllerTestConfig implements WebMvcConfigurer {
    @MockBean
    GroupRepository groupRepository;
    @MockBean
    SimpleHostCreator simpleHostCreator;
    @MockBean
    GroupCreateService groupCreateService;
    @MockBean
    GroupManagingService groupManagingService;
    @MockBean
    GroupReadService groupReadService;
    @MockBean
    GroupJoinFacade groupJoinFacade;

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
