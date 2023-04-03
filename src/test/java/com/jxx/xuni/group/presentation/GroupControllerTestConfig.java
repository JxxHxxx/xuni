package com.jxx.xuni.group.presentation;

import com.jxx.xuni.TestLoginFilter;
import com.jxx.xuni.auth.config.LoginFilter;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.SimpleHostCreator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class GroupControllerTestConfig {
    @MockBean
    GroupRepository groupRepository;
    @MockBean
    SimpleHostCreator simpleHostCreator;
    @Bean
    GroupCreateService groupCreateService() {
        return new GroupCreateService(groupRepository, simpleHostCreator);
    }
    @Bean
    LoginFilter loginFilter() {
        return new TestLoginFilter();
    }
}
