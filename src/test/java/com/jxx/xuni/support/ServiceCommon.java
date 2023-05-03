package com.jxx.xuni.support;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;


public class ServiceCommon {
    @MockBean
    JavaMailSender mailSender;
}
