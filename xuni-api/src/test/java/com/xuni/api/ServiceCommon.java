package com.xuni.api.support;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;


public class ServiceCommon {
    @MockBean
    JavaMailSender mailSender;
    @MockBean
    CacheManager cacheManager;
}
