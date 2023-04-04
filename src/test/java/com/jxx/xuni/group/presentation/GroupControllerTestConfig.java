package com.jxx.xuni.group.presentation;

import com.jxx.xuni.TestAuthInterceptor;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.SimpleHostCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("[Register Test Interceptor]");
        registry.addInterceptor(new TestAuthInterceptor())
                .order(1);
    }
}
