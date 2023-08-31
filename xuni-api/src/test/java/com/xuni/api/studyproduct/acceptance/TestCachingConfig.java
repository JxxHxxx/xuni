package com.xuni.api.studyproduct.acceptance;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

@TestConfiguration
public class TestCachingConfig {

    @MockBean
    JavaMailSender mailSender;

    @Bean
    public CacheManager localCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("study-products", "study-product", "study-product-category"));
        return cacheManager;
    }
}
