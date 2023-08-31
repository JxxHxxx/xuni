package com.xuni.api.group.presentation;

import com.xuni.api.group.application.GroupCreateService;
import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.api.group.dto.response.GroupApiMessage;
import com.xuni.common.http.SimpleResponse;
import com.xuni.api.group.dto.request.GroupCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.xuni.api.group.dto.response.GroupApiMessage.*;

@RestController
@RequiredArgsConstructor
public class GroupCreateController {

    private final GroupCreateService groupCreateService;

    @PostMapping("/groups")
    public ResponseEntity<SimpleResponse> create(@RequestBody @Validated GroupCreateForm groupCreateForm,
                                                 @AuthenticatedMember MemberDetails memberDetails) {
        Long groupId = groupCreateService.create(memberDetails.getUserId(), groupCreateForm);

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(httpHeaders -> httpHeaders.add(HttpHeaders.LOCATION, "/groups/" + groupId))
                .body(SimpleResponse.create(GROUP_CREATED));
    }
}
