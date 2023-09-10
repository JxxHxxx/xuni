package com.xuni.api.auth.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.application.jwt.JwtTokenManager;
import com.xuni.core.common.http.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.xuni.api.auth.dto.response.AuthResponseMessage.*;

@RestController
@RequiredArgsConstructor
public class ApiGatewayController {

    private final JwtTokenManager jwtTokenManager;

    @GetMapping("/api/auth")
    public ResponseEntity<DataResponse<MemberDetails>> receiveMemberDetails(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        return ResponseEntity.status(200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new DataResponse(200, READ_MEMBER_DETAILS, jwtTokenManager.getMemberDetails(token)));

    }
}