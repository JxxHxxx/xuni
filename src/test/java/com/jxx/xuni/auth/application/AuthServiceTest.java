package com.jxx.xuni.auth.application;

import com.jxx.xuni.auth.dto.request.AuthCodeForm;
import com.jxx.xuni.auth.dto.request.EmailForm;
import com.jxx.xuni.auth.dto.request.LoginForm;
import com.jxx.xuni.auth.dto.request.SignupForm;
import com.jxx.xuni.auth.dto.response.CreateAuthCodeEvent;
import com.jxx.xuni.auth.dto.response.VerifyAuthCodeEvent;
import com.jxx.xuni.member.domain.*;
import com.jxx.xuni.support.ServiceCommon;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.EXISTED_EMAIL;
import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN_FAIL;
import static com.jxx.xuni.member.domain.exception.ExceptionMessage.ALREADY_EXIST_EMAIL;
import static com.jxx.xuni.member.domain.exception.ExceptionMessage.NOT_EXIST_AUTH_CODE;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class AuthServiceTest extends ServiceCommon {
    @Autowired
    AuthService authService;
    @Autowired
    AuthCodeRepository authCodeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    String AuthCodeValue;
    String authCodeId;

    @BeforeEach
    void beforeEach() {
        AuthCode authCode = new AuthCode("test@naver.com", UsageType.SIGNUP);
        AuthCodeValue = authCode.getValue();
        authCodeId = authCodeRepository.save(authCode).getAuthCodeId();
    }

    @DisplayName("인증 코드를 정상적으로 생성하면 " +
            "CreateAuthCodeEvent 객체를 반환한다. " +
            "해당 객체에는 입력한 이메일을 가지고 있으며 value, authCodeId를 가져온다." +
            "또한 입력한 이메일을 가진 인증 코드 객체가 영속화되어야 한다.")
    @Test
    void create_auth_code_success_first_request() {
        //given
        EmailForm emailForm = new EmailForm("create@naver.com");
        //when
        CreateAuthCodeEvent createAuthCodeEvent = authService.createAuthCode(emailForm);
        //then
        assertThat(createAuthCodeEvent.value()).isNotNull();
        assertThat(createAuthCodeEvent.authCodeId()).isNotNull();
        assertThat(createAuthCodeEvent.email()).isEqualTo("create@naver.com");
        assertThat(authCodeRepository.findAll().stream().filter(a -> a.getEmail().equals("create@naver.com")).findFirst()).isPresent();
    }

    @DisplayName("동일한 용도, 동일한 이메일의 인증 코드가 이미 존재할 때 인증 코드를 요청하면 " +
            "기존에 있는 인증 코드 객체에서 인증 코드 값만 변경된다.")
    @Test
    void create_auth_code_success_second_request() {
        //given
        EmailForm emailForm = new EmailForm("test@naver.com");

        //when
        authService.createAuthCode(emailForm);
        List<AuthCode> authCodes = authCodeRepository.findAll().stream()
                .filter(a -> a.getEmail().equals("test@naver.com")).toList();

        //then
        assertThat(authCodes.size()).isEqualTo(1);
        String updateAuthCodeValue = authCodes.get(0).getValue();
        assertThat(AuthCodeValue).isNotEqualTo(updateAuthCodeValue);
    }

    @DisplayName("이메일 중복 검증을 통과할 경우 어떠한 예외도 발생하지 않는다.")
    @Test
    void check_exist_email_success() {
        //given
        EmailForm emailForm = new EmailForm("create@naver.com");
        //when - then
        assertThatCode(() -> authService.checkExistEmail(emailForm))
                .doesNotThrowAnyException();
    }

    @DisplayName("이미 회원 DB에 존재하는 이메일로 검증을 수행할 경우" +
            "IllegalArgumentException 예외 발생" +
            "예외 메세지 ALREADY_EXIST_EMAIL " +
            "" +
            "해당 메서드는 회원 가입을 위해 인증 코드를 발급할 때 한하여 사용한다.")
    @Test
    void check_exist_email_fail_cause_exist_email() {
        //given
        EmailForm emailForm = new EmailForm("already@naver.com");
        memberRepository.save(new Member(LoginInfo.of("already@naver.com", "0000"),"김유니"));
        //when - then
        assertThatThrownBy(() -> authService.checkExistEmail(emailForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_EXIST_EMAIL);
    }

    @DisplayName("인증 코드 인증을 정상적으로 수행하면" +
            "VerifyAuthCodeEvent 객체를 반환한다" +
            "해당 객체는 인증 코드 식별자를 가지고 있다" +
            "또한 해당 인증 코드 엔티티 isAuthenticated 상태가 true로 영속화 되어야 한다")
    @Test
    void verify_auth_code_success() {
        //given
        AuthCodeForm authCodeForm = new AuthCodeForm(authCodeId, AuthCodeValue);
        //when
        VerifyAuthCodeEvent verifyAuthCodeEvent = authService.verifyAuthCode(authCodeForm);
        //then
        assertThat(verifyAuthCodeEvent.authCodeId()).isEqualTo(authCodeId);
        assertThat(authCodeRepository.findById(authCodeId).get().isAuthenticated()).isTrue();
    }

    @DisplayName("존재하지 않는 인증 코드 식별자로 인증 코드를 검증하려는 경우" +
            "IllegalArgumentException 예외 발생" +
            "예외 메세지 NOT_EXIST_AUTH_CODE" +
            "인증 코드 엔티티 isAuthenticated 상태는 false로 영속화 되어야 한다")
    @Test
    void verify_auth_code_fail_cause_not_exist_auth_code() {
        //given
        String notExistAuthCodeId = "not-exist-auth-code";
        AuthCodeForm authCodeForm = new AuthCodeForm(notExistAuthCodeId, AuthCodeValue);
        //when - then
        assertThatThrownBy(() -> authService.verifyAuthCode(authCodeForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_AUTH_CODE);

        assertThat(authCodeRepository.findById(authCodeId).get().isAuthenticated()).isFalse();
    }

    @DisplayName("회원가입 성공하면 " +
            "Member 객체가 저장되어야 한다")
    @Test
    void signup_success() {
        //given
        AuthCodeForm form = new AuthCodeForm(authCodeId, AuthCodeValue);
        authService.verifyAuthCode(form);

        SignupForm signupForm = new SignupForm(authCodeId, "0000", "이재헌");
        // when - then
        assertThatCode(() -> authService.signup(signupForm))
                .doesNotThrowAnyException();

        assertThat(memberRepository.findByLoginInfoEmail("test@naver.com").get()).isNotNull();
    }
    @DisplayName("존재하지 않는 인증 코드로 인증을 시도할 경우 " +
            "IllegalArgumentException 예외 발생" +
            "예외 메시지 NOT_EXIST_AUTH_CODE")
    @Test
    void signup_fail_cause_not_exist_auth_code() {
        //given
        String notExistAuthCodeId = "not-exist-auth-code-id";
        SignupForm signupForm = new SignupForm(notExistAuthCodeId, "0000", "이재헌");
        //when - then
        assertThatThrownBy(() -> authService.signup(signupForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_EXIST_AUTH_CODE);
    }

    @DisplayName("중복된 이메일로 회원 가입을 시도하려는 경우" +
            "IllegalArgumentException 예외 발생 " +
            "예외 메시지 EXISTED_EMAIL")
    @Test
    void signup_fail_cause_duplicated_email() {
        //given
        AuthCodeForm form = new AuthCodeForm(authCodeId, AuthCodeValue);
        authService.verifyAuthCode(form);
        SignupForm signupForm = new SignupForm(authCodeId, "0000", "김유니");
        // 이메일 중복을 만들기 위한 데이터 주입
        memberRepository.save(new Member(LoginInfo.of("test@naver.com","0000"),"김유니"));

        //when - then
        assertThatThrownBy(() -> authService.signup(signupForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EXISTED_EMAIL);
    }

    @DisplayName("로그인 성공 시 사용자의 Name, Email, UserId 을 반환한다.")
    @Test
    void login_success() {
        // given
        memberRepository.save(new Member(LoginInfo.of("test@naver.com", passwordEncoder.encrypt("0000")), "김유니"));
        LoginForm loginForm = new LoginForm("test@naver.com", "0000");
        // when
        MemberDetails memberDetails = authService.login(loginForm);
        // then
        assertThat(memberDetails.getName()).isEqualTo("김유니");
        assertThat(memberDetails.getEmail()).isEqualTo("test@naver.com");
        assertThat(memberDetails.getUserId()).isNotNull();
    }

    @DisplayName("로그인 실패 케이스")
    @ParameterizedTest(name ="[{index}] : {2}")
    @CsvSource(value = {"test@naver.com, 1235, 비밀번호가 틀린 경우 ",
                        "incorrectemail@naver.com, 0000, 이메일이 틀린 경우"})
    void login_fail_by_message(String email, String password, String message) {
        // given
        memberRepository.save(new Member(LoginInfo.of("test@naver.com", passwordEncoder.encrypt("0000")), "김유니"));
        LoginForm loginForm = new LoginForm(email, password);
        // when - then
        assertThatThrownBy(() -> authService.login(loginForm))
                .hasMessage(LOGIN_FAIL);

    }
}