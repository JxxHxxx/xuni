package com.jxx.xuni.auth.application;

import com.jxx.xuni.auth.dto.request.*;
import com.jxx.xuni.auth.dto.response.CreateAuthCodeEvent;
import com.jxx.xuni.auth.dto.response.VerifyAuthCodeEvent;
import com.jxx.xuni.member.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.EXISTED_EMAIL;
import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.LOGIN_FAIL;
import static com.jxx.xuni.member.domain.exception.ExceptionMessage.ALREADY_EXIST_EMAIL;
import static com.jxx.xuni.member.domain.exception.ExceptionMessage.NOT_EXIST_AUTH_CODE;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthCodeRepository authCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateAuthCodeEvent createAuthCode(EmailForm form) {
        AuthCode authCode = authCodeRepository.save(new AuthCode(form.email(), UsageType.SIGNUP));

        return new CreateAuthCodeEvent(authCode.getAuthCodeId(), authCode.getEmail(), authCode.getValue());
    }

    public void checkExistEmail(EmailForm form) {
        if (memberRepository.findByLoginInfoEmail(form.email()).isPresent()) throw new IllegalArgumentException(ALREADY_EXIST_EMAIL);
    }

    @Transactional
    public VerifyAuthCodeEvent verifyAuthCode(AuthCodeForm form) {
        AuthCode authCode = authCodeRepository.findById(form.authCodeId()).orElseThrow(
                () -> new IllegalArgumentException(NOT_EXIST_AUTH_CODE));

        authCode.verifyAuthCode(form.value());

        return new VerifyAuthCodeEvent(authCode.getAuthCodeId());
    }

    @Transactional
    public void signup(SignupForm form) {
        AuthCode authCode = authCodeRepository.findById(form.getAuthCodeId()).orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_AUTH_CODE));
        checkDuplicationOfEmail(authCode.getEmail());

        Member member = authCode.createMember(authCode.getEmail(), passwordEncoder.encrypt(form.getPassword()), form.getName());
        Member saveMember = memberRepository.save(member);
        authCode.setMember(saveMember);
    }

    public MemberDetails login(LoginForm loginForm) {
        Member member = memberRepository.findByLoginInfoEmail(loginForm.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(LOGIN_FAIL));

        member.matches(passwordEncoder.encrypt(loginForm.getPassword()));

        return new SimpleMemberDetails(member.getId(), member.receiveEmail(), member.getName(), member.getAuthority());
    }

    private void checkDuplicationOfEmail(String email) {
        if (memberRepository.findByLoginInfoEmail(email).isPresent()) {
            throw new IllegalArgumentException(EXISTED_EMAIL);
        }
    }
}
