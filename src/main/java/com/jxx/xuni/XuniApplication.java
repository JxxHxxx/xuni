package com.jxx.xuni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class XuniApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuniApplication.class, args);
    }

}
