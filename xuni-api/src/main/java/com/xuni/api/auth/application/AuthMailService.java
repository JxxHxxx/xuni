package com.xuni.api.auth.application;

import com.xuni.api.auth.dto.response.CreateAuthCodeEvent;
import com.xuni.api.common.service.SimpleMailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthMailService {
    private final SimpleMailHandler mailHandler;

    @Transactional
    public void sendAuthCode(CreateAuthCodeEvent event) {
        MimeMessagePreparator message = prepareAuthCodeMessage(event.email(), event.value());
        mailHandler.send(message);
    }

    private MimeMessagePreparator prepareAuthCodeMessage(String email, String code) {
        return mailHandler.prepareMessage(email,
                "xuni 회원 가입 인증 번호",
                "인증 번호 : " + code + " 입니다. 유효 시간은 30분 입니다.");
    }
}
