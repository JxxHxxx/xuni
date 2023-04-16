package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.group.application.GroupJoinFacade;
import com.jxx.xuni.group.application.GroupManagingService;
import com.jxx.xuni.group.dto.request.StudyCheckForm;
import com.jxx.xuni.group.dto.response.GroupApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupManagingController {
    private final GroupManagingService groupManagingService;
    private final GroupJoinFacade groupJoinFacade;

    @PostMapping("/groups/{group-id}/join")
    public ResponseEntity<GroupApiSimpleResult> join(@PathVariable("group-id") Long groupId,
                                                     @AuthenticatedMember MemberDetails memberDetails) {

        groupJoinFacade.join(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.join());
    }

    @PostMapping("/groups/{group-id}/close-recruitment")
    public ResponseEntity<GroupApiSimpleResult> close(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.closeRecruitment(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.closeRecruitment());
    }

    @PostMapping("/groups/{group-id}/start")
    public ResponseEntity<GroupApiSimpleResult> start(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails,
                                                      @RequestBody List<StudyCheckForm> studyCheckForms) {

        groupManagingService.start(groupId, memberDetails, studyCheckForms);
        return ResponseEntity.ok(GroupApiSimpleResult.start());
    }
}
