package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import com.jxx.xuni.group.dto.response.GroupCreateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupCreateController {

    private final GroupCreateService groupCreateService;

    @PostMapping("/groups")
    public ResponseEntity<GroupCreateResult> create(@SessionAttribute("loginMember") MemberDetails memberDetails,
                                                    @RequestBody GroupCreateForm groupCreateForm) {
        groupCreateService.create(memberDetails.getUserId(), groupCreateForm);
        return new ResponseEntity(GroupCreateResult.created(), CREATED);
    }
}
