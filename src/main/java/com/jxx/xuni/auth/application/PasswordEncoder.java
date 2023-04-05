package com.jxx.xuni.auth.application;

public interface PasswordEncoder {
    String encrypt(String password);
}
