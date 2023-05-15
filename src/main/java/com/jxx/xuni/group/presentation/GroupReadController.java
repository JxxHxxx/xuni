package com.jxx.xuni.group.presentation;

import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.dto.response.*;
import com.jxx.xuni.group.query.GroupAllQueryResponse;
import com.jxx.xuni.group.query.GroupSearchCondition;
import com.jxx.xuni.group.query.PageConverter;
import com.jxx.xuni.studyproduct.domain.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.*;

@Slf4j
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
    public ResponseEntity<GroupApiReadResult> readOne(@PathVariable("group-id") Long groupId) {
        GroupReadOneResponse response = groupReadService.readOne(groupId);

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ONE_READ, response));
    }

    @GetMapping("/groups/cond")
    public ResponseEntity<GroupApiReadResult> readCond(@RequestParam("category") Category category) {
        List<GroupReadAllResponse> response = groupReadService.readByCategory(category);
        return ResponseEntity.ok(new GroupApiReadResult(GROUP_CATEGORY_READ, response));
    }

    @GetMapping("/v2/groups")
    public ResponseEntity<GroupPageApiResult> readCondV2(GroupSearchCondition condition, Pageable pageable) {
        Page<GroupAllQueryResponse> page = groupReadService.searchGroup(condition, pageable);
        List<GroupAllQueryResponse> response = page.getContent();
        PageInfo pageInfo = pageConverter.toPageInfo(page.getPageable(), page.getTotalElements(), page.getTotalPages());

        return ResponseEntity.ok(new GroupPageApiResult(SEARCH_GROUP_COND, response, pageInfo));
    }
}
