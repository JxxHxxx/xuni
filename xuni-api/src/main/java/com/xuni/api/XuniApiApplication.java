package com.xuni.api;

import com.xuni.XuniCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 다른 모듈에 스프링 빈이 존재하기 때문에 scanBasePackageClasses 를 잡아줘야한다.
 */
@SpringBootApplication(scanBasePackageClasses = {XuniCore.class, XuniApiApplication.class})
public class XuniApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuniApiApplication.class, args);
    }

}
