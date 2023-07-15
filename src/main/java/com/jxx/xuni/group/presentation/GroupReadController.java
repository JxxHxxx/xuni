package com.jxx.xuni.group.presentation;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.auth.presentation.AuthenticatedMember;
import com.jxx.xuni.auth.presentation.OptionalAuthentication;
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

@RestController
@RequiredArgsConstructor
public class GroupReadController {

    private final GroupReadService groupReadService;
    private final PageConverter pageConverter;

    @GetMapping("/groups")
    public ResponseEntity<GroupApiReadResult> readAll() {
        List<GroupReadAllResponse> response = groupReadService.readAll();

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ALL_READ, response));
    }

    @GetMapping("/groups/{group-id}")
    public ResponseEntity<GroupApiReadResult> readOne(@PathVariable("group-id") Long groupId,
                                                      @OptionalAuthentication MemberDetails memberDetails) {

        GroupReadOneResponse response = groupReadService.readOne(groupId, memberDetails.getUserId());

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ONE_READ, response));
    }

    @GetMapping("/groups/cd-sp")
    public ResponseEntity<GroupApiReadResult> readCond(@RequestParam("category") Category category) {
        List<GroupReadAllResponse> response = groupReadService.readByCategory(category);
        return ResponseEntity.ok(new GroupApiReadResult(GROUP_CATEGORY_READ, response));
    }

    @GetMapping("/groups/cd-cp")
    public ResponseEntity<GroupPageApiResult> searchCond(@ModelAttribute GroupSearchCondition condition,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {

        Page<GroupAllQueryResponse> pageResponse = groupReadService.searchGroup(condition, page, size);
        List<GroupAllQueryResponse> content = pageResponse.getContent();
        PageInfo pageInfo = pageConverter.toPageInfo(pageResponse);

        return ResponseEntity.ok(new GroupPageApiResult(SEARCH_GROUP_COND, content, pageInfo));
    }

    @GetMapping("/groups/{group-id}/chapters")
    public ResponseEntity<GroupApiReadResult> readChapter(@PathVariable("group-id") Long groupId,
                                                          @AuthenticatedMember MemberDetails memberDetails) {
        List<GroupStudyCheckResponse> response = groupReadService.readGroupTask(groupId, memberDetails.getUserId());

        return ResponseEntity.ok(new GroupApiReadResult(STUDY_CHECK_OF_GROUP_MEMBER, response));
    }

    @GetMapping("/members/{member-id}/groups")
    public ResponseEntity<GroupApiReadResult> readOwnBySelf(@PathVariable("member-id") Long groupMemberId,
                                                            @AuthenticatedMember MemberDetails md) {
        List<GroupAllQueryResponse> response = groupReadService.readOwn(groupMemberId);
        return ResponseEntity.ok(new GroupApiReadResult(GROUP_OWN_READ, response));
    }
}
