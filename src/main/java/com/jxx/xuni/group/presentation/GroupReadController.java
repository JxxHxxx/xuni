package com.jxx.xuni.group.presentation;

import com.jxx.xuni.group.application.GroupReadService;
import com.jxx.xuni.group.dto.response.GroupApiReadResult;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.jxx.xuni.group.dto.response.GroupApiMessage.GROUP_ALL_READ;

@RestController
@RequiredArgsConstructor
public class GroupReadController {

    private final GroupReadService groupReadService;

    @GetMapping("/groups")
    public ResponseEntity<GroupApiReadResult> readAll() {
        List<GroupReadAllResponse> response = groupReadService.readAll();

        return ResponseEntity.ok(new GroupApiReadResult(GROUP_ALL_READ, response));
    }
}
