package com.xuni.api.auth.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("패스워드 암호화 테스트")
    @Test
    void encrypt() {
        //when
        String encrypt = passwordEncoder.encrypt("test");
        //then
        Assertions.assertThat(encrypt).isNotEqualTo("test");
    }
}