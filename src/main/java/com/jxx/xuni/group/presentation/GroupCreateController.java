package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.group.dto.response.GroupApiSimpleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class GroupCreateController {

    private final GroupCreateService groupCreateService;

    @PostMapping("/groups")
    public ResponseEntity<GroupApiSimpleResult> create(@AuthenticatedMember MemberDetails memberDetails,
                                                       @RequestBody @Validated GroupCreateForm groupCreateForm) {

        groupCreateService.create(memberDetails.getUserId(), groupCreateForm);
        return new ResponseEntity(GroupApiSimpleResult.createGroup(), CREATED);
    }
}
