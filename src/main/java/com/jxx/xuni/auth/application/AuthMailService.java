package com.jxx.xuni.auth.application;

import com.jxx.xuni.auth.dto.request.AuthCodeForm;
import com.jxx.xuni.common.service.SimpleMailHandler;
import com.jxx.xuni.member.domain.AuthCode;
import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.domain.MemberRepository;
import com.jxx.xuni.member.domain.UsageType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthMailService {
    private final SimpleMailHandler mailHandler;
    private final MemberRepository memberRepository;

    @Transactional
    public void sendAuthCode(String email) {
        String code = AuthCode.generateValue();
        // (1) 코드 정보를 영속화 - 에러 발생 시 예외
        Member member = memberRepository.findByLoginInfoEmail(email).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
        member.createAuthCode(new AuthCode(code, UsageType.SIGNUP));
        // (2) 이메일 전송
        MimeMessagePreparator message = prepareAuthCodeMessage(email, code);

        if (email.equals("jsmtmt@naver.com")) throw new MailException("이메일 전송 예외") {
            @Override
            public Throwable getRootCause() {
                return super.getRootCause();
            }
        };

        mailHandler.send(message);
    }

    @Transactional
    public void verifyAuthCode(AuthCodeForm form) {
        Member member = memberRepository.findById(form.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
        member.verifySignUpAuthCode(form.code());
    }

    private MimeMessagePreparator prepareAuthCodeMessage(String email, String code) {
        return mailHandler.prepareMessage(email,
                "xuni 회원 가입 인증 번호",
                "인증 번호 : " + code + " 입니다. 유효 시간은 30분 입니다.");
    }
}
