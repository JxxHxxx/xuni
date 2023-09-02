package com.xuni.api.group.presentation;

import com.xuni.api.auth.application.MemberDetails;
import com.xuni.api.auth.presentation.AuthenticatedMember;
import com.xuni.api.auth.presentation.OptionalAuthentication;
import com.xuni.api.group.dto.response.GroupReadAllResponse;
import com.xuni.api.group.dto.response.GroupReadOneResponse;
import com.xuni.api.group.dto.response.GroupStudyCheckResponse;
import com.xuni.common.http.DataResponse;
import com.xuni.common.http.PageResponse;
import com.xuni.common.query.PageInfo;
import com.xuni.api.group.application.GroupReadService;
import com.xuni.api.group.query.GroupAllQueryResponse;
import com.xuni.api.group.query.dynamic.GroupSearchCondition;
import com.xuni.common.query.PageConverter;
import com.xuni.common.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xuni.api.group.dto.response.GroupApiMessage.*;

@RestController
@RequiredArgsConstructor
public class GroupReadController {

    private final GroupReadService groupReadService;
    private final PageConverter pageConverter;

    @GetMapping("/groups")
    public ResponseEntity<DataResponse> readAll() {
        List<GroupReadAllResponse> response = groupReadService.readAll();
        DataResponse<List<GroupReadAllResponse>> body = new DataResponse(200, GROUP_ALL_READ, response);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/groups/{group-id}")
    public ResponseEntity<DataResponse> readOne(@PathVariable("group-id") Long groupId,
                                                @OptionalAuthentication MemberDetails memberDetails) {

        GroupReadOneResponse response = groupReadService.readOne(groupId, memberDetails.getUserId());
        DataResponse body = new DataResponse(200, GROUP_ONE_READ, response);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/groups/cd-sp")
    public ResponseEntity<DataResponse> readCond(@RequestParam("category") Category category) {
        List<GroupReadAllResponse> response = groupReadService.readByCategory(category);
        DataResponse body = new DataResponse(200, GROUP_CATEGORY_READ, response);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/groups/cd-cp")
    public ResponseEntity<PageResponse> searchCond(@ModelAttribute GroupSearchCondition condition,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {

        Page<GroupAllQueryResponse> pageResponse = groupReadService.searchGroup(condition, page, size);
        List<GroupAllQueryResponse> content = pageResponse.getContent();
        PageInfo pageInfo = pageConverter.toPageInfo(pageResponse);

        PageResponse<GroupAllQueryResponse, PageInfo> body = new PageResponse<>(SEARCH_GROUP_COND, content, pageInfo);

        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

    @GetMapping("/groups/{group-id}/chapters")
    public ResponseEntity<DataResponse> readChapter(@PathVariable("group-id") Long groupId,
                                                    @AuthenticatedMember MemberDetails memberDetails) {
        List<GroupStudyCheckResponse> response = groupReadService.readGroupTask(groupId, memberDetails.getUserId());
        DataResponse body = new DataResponse(200, STUDY_CHECK_OF_GROUP_MEMBER, response);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/members/{member-id}/groups")
    public ResponseEntity<DataResponse> readOwnBySelf(@PathVariable("member-id") Long groupMemberId,
                                                      @AuthenticatedMember MemberDetails md) {
        List<GroupAllQueryResponse> response = groupReadService.readOwn(groupMemberId);
        DataResponse body = new DataResponse(200, GROUP_OWN_READ, response);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
