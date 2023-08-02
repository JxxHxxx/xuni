package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.common.http.SimpleResponseBody;
import com.jxx.xuni.group.application.GroupCreateService;
import com.jxx.xuni.group.dto.request.GroupCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_CREATED;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class GroupCreateController {

    private final GroupCreateService groupCreateService;

    @PostMapping("/groups")
    public ResponseEntity<SimpleResponseBody> create(@RequestBody @Validated GroupCreateForm groupCreateForm,
                                                     @AuthenticatedMember MemberDetails memberDetails) {
        Long groupId = groupCreateService.create(memberDetails.getUserId(), groupCreateForm);

        return ResponseEntity.status(CREATED)
                .headers(httpHeaders -> httpHeaders.add(LOCATION, "/groups/" + groupId))
                .body(SimpleResponseBody.create(GROUP_CREATED));
    }
}
