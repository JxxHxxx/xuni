package com.jxx.xuni.group.presentation;

import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.dto.response.GroupApiReadResult;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.studyproduct.domain.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_ALL_READ;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupReadController {

    private final GroupReadService groupReadService;

    @GetMapping("/groups")
    public ResponseEntity<GroupApiReadResult> readAll() {
        List<GroupReadAllResponse> response = groupReadService.readAll();

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ALL_READ, response));
    }

    @GetMapping("/groups/{group-id}")
    public ResponseEntity<GroupApiReadResult> readOne(@PathVariable("group-id") Long groupId) {
        GroupReadAllResponse response = groupReadService.readOne(groupId);

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ALL_READ, response));
    }

    @GetMapping("/groups/cond")
    public ResponseEntity<GroupApiReadResult> readCond(@RequestParam("category") Category category) {
        List<GroupReadAllResponse> response = groupReadService.readByCategory(category);
        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ALL_READ, response));
    }
}
