package com.jxx.xuni.auth.presentation.gateway;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.common.http.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.jxx.xuni.auth.dto.response.AuthResponseMessage.*;
import static org.springframework.http.HttpHeaders.*;

@RestController
@RequiredArgsConstructor
public class ApiGatewayController {

    private final JwtTokenManager jwtTokenManager;

    @GetMapping("/api/auth")
    public ResponseEntity<DataResponse<MemberDetails>> receiveMemberDetails(@RequestHeader(name = AUTHORIZATION) String token) {

        return ResponseEntity.status(200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new DataResponse(200, READ_MEMBER_DETAILS, jwtTokenManager.getMemberDetails(token)));

    }
}