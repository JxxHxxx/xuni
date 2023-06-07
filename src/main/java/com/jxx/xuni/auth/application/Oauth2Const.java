package com.jxx.xuni.auth.application;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Oauth2Const {
    public static final String REDIRECT_URL_VALUE = "http://localhost:8080/login/oauth2/code/google";
    public static final String RESPONSE_TYPE_VALUE = "code";
    public static final String GRANT_TYPE_VALUE = "authorization_code";

    public static final String AUTH_CODE_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth?";
    public static final String ACCESS_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token";
    public static final String USER_INFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo";
}
