package com.xuni.api.group.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.api.group.application.GroupJoinFacade;
import com.xuni.api.group.application.GroupManagingService;
import com.xuni.api.group.dto.response.GroupApiSimpleResult;
import com.xuni.group.domain.GroupTaskForm;
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

    @PatchMapping("/groups/{group-id}/leave")
    public ResponseEntity<GroupApiSimpleResult> leave(@PathVariable("group-id") Long groupId,
                                                     @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.leave(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.leave());
    }

    @PatchMapping("/groups/{group-id}/closing-recruitment")
    public ResponseEntity<GroupApiSimpleResult> close(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.closeRecruitment(memberDetails, groupId);
        return ResponseEntity.ok(GroupApiSimpleResult.closeRecruitment());
    }

    @PostMapping("/groups/{group-id}/start")
    public ResponseEntity<GroupApiSimpleResult> start(@PathVariable ("group-id") Long groupId,
                                                      @AuthenticatedMember MemberDetails memberDetails,
                                                      @RequestBody List<GroupTaskForm> studyCheckForms) {

        groupManagingService.start(groupId, memberDetails, studyCheckForms);
        return ResponseEntity.ok(GroupApiSimpleResult.start());
    }

    @PatchMapping("/groups/{group-id}/chapters/{chapter-id}")
    public ResponseEntity<GroupApiSimpleResult> check(@PathVariable ("group-id") Long groupId,
                                                      @PathVariable ("chapter-id") Long chapterId,
                                                      @AuthenticatedMember MemberDetails memberDetails) {

        groupManagingService.doTask(memberDetails, groupId, chapterId);
        return ResponseEntity.ok(GroupApiSimpleResult.check());
    }
}
