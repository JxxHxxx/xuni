package com.jxx.xuni.common.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleMailHandler {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public MimeMessagePreparator prepareMessage(String mailOfReceiver, String subject, String message) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipients(Message.RecipientType.TO, mailOfReceiver);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
        };
        return preparator;
    }

    public void send(MimeMessagePreparator mimeMessage) {
        try {
            mailSender.send(mimeMessage);
        } catch (MailException e) {
            log.error("이메일 전송 에러 : {}", e.getMessage());
        }
    }


}
