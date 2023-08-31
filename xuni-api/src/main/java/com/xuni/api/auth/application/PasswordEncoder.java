package com.xuni.api.auth.application;

public interface PasswordEncoder {
    String encrypt(String password);
}
