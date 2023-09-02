package com.xuni.api.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuni.api.auth.infra.MemberRepository;
import com.xuni.auth.domain.LoginInfo;
import com.xuni.auth.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.xuni.api.auth.application.Oauth2Const.*;
import static com.xuni.auth.domain.AuthProvider.*;

@Component
@RequiredArgsConstructor
public class GoogleClient  {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    public String CLIENT_ID_VALUE;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public String CLIENT_SECRET_VALUE;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    public String SCOPE_VALUE;

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public String getAuthCodeUrl() {
        StringBuffer urlBuilder = new StringBuffer(AUTH_CODE_ENDPOINT);
        urlBuilder.append("client_id=").append(CLIENT_ID_VALUE).append("&");
        urlBuilder.append("redirect_uri=").append(REDIRECT_URL_VALUE).append("&");
        urlBuilder.append("response_type=").append(RESPONSE_TYPE_VALUE).append("&");
        urlBuilder.append("scope=").append(SCOPE_VALUE);

        return urlBuilder.toString();
    }

    public MemberDetails login(String authCode) {
        HttpEntity<MultiValueMap<String, String>> request = createGetTokenRequest(authCode);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(ACCESS_TOKEN_ENDPOINT, request, String.class);

        String accessToken = extractAccessToken(response);

        JsonNode userInfo = requestUserInfo(accessToken);
        return verify(userInfo);
    }

    private JsonNode requestUserInfo(String accessToken) {
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        JsonNode userInfoJson;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(USER_INFO_ENDPOINT, HttpMethod.GET, userInfoRequest, String.class);
        try {
            userInfoJson = objectMapper.readTree(userInfoResponse.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userInfoJson;
    }

    private String extractAccessToken(ResponseEntity<String> response) {
        String accessToken;
        try {
            accessToken = objectMapper.readTree(response.getBody()).get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }

    private HttpEntity<MultiValueMap<String, String>> createGetTokenRequest(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authCode);
        params.add("client_id", CLIENT_ID_VALUE);
        params.add("client_secret", CLIENT_SECRET_VALUE);
        params.add("redirect_uri", REDIRECT_URL_VALUE);
        params.add("grant_type", GRANT_TYPE_VALUE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(params, headers);
    }

    private MemberDetails verify(JsonNode userInfo) {
        String email = userInfo.get("email").asText();
        String name = userInfo.get("name").asText();
        Optional<Member> oMember = memberRepository.findByLoginInfoEmail(email);

        if (oMember.isPresent()) {
            Member member = oMember.get();
            if (member.isAuthProvider(GOOGLE)) {
                return new SimpleMemberDetails(member.getId(), member.receiveEmail(), member.getName(), member.getAuthority());
            }
        }

        Member newMember = createNewMember(email, name);
        Member saveMember = memberRepository.save(newMember);
        return new SimpleMemberDetails(saveMember.getId(), saveMember.receiveEmail(), saveMember.getName(), saveMember.getAuthority());
    }

    private Member createNewMember(String email, String name) {
        return new Member(LoginInfo.of(email, passwordEncoder.encrypt(UUID.randomUUID().toString())), name, GOOGLE);
    }
}
