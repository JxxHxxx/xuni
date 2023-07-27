package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.auth.presentation.OptionalAuthentication;
import com.jxx.xuni.common.http.DataResponseBody;
import com.jxx.xuni.common.http.PageResponseBody;
import com.jxx.xuni.common.query.PageInfo;
import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.dto.response.*;
import com.jxx.xuni.group.query.dynamic.GroupSearchCondition;
import com.jxx.xuni.common.query.PageConverter;
import com.jxx.xuni.common.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class GroupReadController {

    private final GroupReadService groupReadService;
    private final PageConverter pageConverter;

    @GetMapping("/groups")
    public ResponseEntity<DataResponseBody> readAll() {
        List<GroupReadAllResponse> response = groupReadService.readAll();
        DataResponseBody<List<GroupReadAllResponse>> body = new DataResponseBody(200, GROUP_ALL_READ, response);

        return ResponseEntity.status(OK).body(body);
    }

    @GetMapping("/groups/{group-id}")
    public ResponseEntity<DataResponseBody> readOne(@PathVariable("group-id") Long groupId,
                                                      @OptionalAuthentication MemberDetails memberDetails) {

        GroupReadOneResponse response = groupReadService.readOne(groupId, memberDetails.getUserId());
        DataResponseBody body = new DataResponseBody(200, GROUP_ONE_READ, response);

        return ResponseEntity.status(OK).body(body);
    }

    @GetMapping("/groups/cd-sp")
    public ResponseEntity<DataResponseBody> readCond(@RequestParam("category") Category category) {
        List<GroupReadAllResponse> response = groupReadService.readByCategory(category);
        DataResponseBody body = new DataResponseBody(200, GROUP_CATEGORY_READ, response);

        return ResponseEntity.status(OK).body(body);
    }

    @GetMapping("/groups/cd-cp")
    public ResponseEntity<PageResponseBody> searchCond(@ModelAttribute GroupSearchCondition condition,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {

        Page<GroupAllQueryResponse> pageResponse = groupReadService.searchGroup(condition, page, size);
        List<GroupAllQueryResponse> content = pageResponse.getContent();
        PageInfo pageInfo = pageConverter.toPageInfo(pageResponse);

        PageResponseBody<GroupAllQueryResponse, PageInfo> body = new PageResponseBody<>(SEARCH_GROUP_COND, content, pageInfo);

        return ResponseEntity.status(OK).body(body);

    }

    @GetMapping("/groups/{group-id}/chapters")
    public ResponseEntity<DataResponseBody> readChapter(@PathVariable("group-id") Long groupId,
                                                          @AuthenticatedMember MemberDetails memberDetails) {
        List<GroupStudyCheckResponse> response = groupReadService.readGroupTask(groupId, memberDetails.getUserId());
        DataResponseBody body = new DataResponseBody(200, STUDY_CHECK_OF_GROUP_MEMBER, response);

        return ResponseEntity.status(OK).body(body);
    }

    @GetMapping("/members/{member-id}/groups")
    public ResponseEntity<DataResponseBody> readOwnBySelf(@PathVariable("member-id") Long groupMemberId,
                                                            @AuthenticatedMember MemberDetails md) {
        List<GroupAllQueryResponse> response = groupReadService.readOwn(groupMemberId);
        DataResponseBody body = new DataResponseBody(200, GROUP_OWN_READ, response);

        return ResponseEntity.status(OK).body(body);
    }
}
