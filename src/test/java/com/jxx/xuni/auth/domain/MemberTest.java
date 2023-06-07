package com.jxx.xuni.auth.domain;

import com.jxx.xuni.auth.domain.LoginInfo;
import com.jxx.xuni.auth.domain.Member;
import com.jxx.xuni.auth.domain.PasswordNotMatchedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.jxx.xuni.auth.domain.AuthProvider.*;
import static com.jxx.xuni.auth.domain.Authority.*;


class MemberTest {

    @DisplayName("멤버 객체 초기화 시 멤버의 권한(Authority)은 USER 다")
    @Test
    void createMember() {
        //given - when
        Member member = new Member(LoginInfo.of("leesin5498@naver.com", "1234"), "이재헌", XUNI);
        //then
        Assertions.assertThat(member.getAuthority()).isEqualTo(USER);
    }

    @DisplayName("Member 엔티티는 비밀번호 검증이 가능하다. 비밀번호가 일치할 경우 어떻나 예외도 발생하지 않는다.")
    @Test
    void matches_password_success() {
        //given - when
        Member member = new Member(LoginInfo.of("leesin5498@naver.com", "1234"), "이재헌",XUNI);
        //then
        Assertions.assertThatCode(() -> member.matches("1234")).doesNotThrowAnyException();
    }

    @DisplayName("비밀번호 검증 시 비밀번호가 일치하지 않는다면 PasswordNotMatchedException 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"9999"})
    void matches_password_fail(String password) {
        //given
        Member member = new Member(LoginInfo.of("leesin5498@naver.com", "1234"), "이재헌",XUNI);

        //when - then
        Assertions.assertThatThrownBy(() -> member.matches(password))
                .isInstanceOf(PasswordNotMatchedException.class);
    }
}