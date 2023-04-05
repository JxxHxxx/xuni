package com.jxx.xuni.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class Sha256PasswordEncoder implements PasswordEncoder{

    private final MessageDigest messageDigest;

    @Override
    public String encrypt(String password) {
        byte[] hash = messageDigest.digest(password.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hash) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
