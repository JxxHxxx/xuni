package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.group.dto.response.GroupApiSimpleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupCreateController {

    private final GroupCreateService groupCreateService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/groups")
    public ResponseEntity<GroupApiSimpleResult> create(HttpServletRequest request,
                                                       @RequestBody GroupCreateForm groupCreateForm) {
        String token = request.getHeader("Authorization");
        String extractedToken = token.substring(7);

        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(extractedToken);
        groupCreateService.create(memberDetails.getUserId(), groupCreateForm);
        return new ResponseEntity(GroupApiSimpleResult.createGroup(), CREATED);
    }
}
