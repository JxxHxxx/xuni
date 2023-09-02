package com.xuni.api;

import com.xuni.XuniCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 다른 모듈에 스프링 빈이 존재하기 때문에 scanBasePackageClasses 를 잡아줘야한다.
 */
@EntityScan(basePackageClasses = XuniCore.class)
@EnableJpaRepositories
@SpringBootApplication(scanBasePackageClasses = {XuniCore.class, XuniApiApplication.class})
public class XuniApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuniApiApplication.class, args);
    }

}
