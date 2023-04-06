package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.support.JwtTokenManager;
import com.jxx.xuni.group.application.GroupManagingService;
import com.jxx.xuni.group.dto.response.GroupApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
public class GroupManagingController {

    private final GroupManagingService groupManagingService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/groups/{group-id}/join")
    public ResponseEntity<GroupApiSimpleResult> join(@PathVariable("group-id") Long groupId, HttpServletRequest request) {
        String extractedToken = request.getHeader("Authorization").substring(7);
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(extractedToken);

        groupManagingService.join(memberDetails, groupId);

        return ResponseEntity.ok(GroupApiSimpleResult.join());
    }

    @PostMapping("/groups/{group-id}/close-recruitment")
    public ResponseEntity<GroupApiSimpleResult> close(@PathVariable ("group-id") Long groupId, HttpServletRequest request) {
        String extractedToken = request.getHeader("Authorization").substring(7);
        MemberDetails memberDetails = jwtTokenManager.getMemberDetails(extractedToken);

        groupManagingService.closeRecruitment(memberDetails, groupId);

        return ResponseEntity.ok(GroupApiSimpleResult.closeRecruitment());
    }
}
