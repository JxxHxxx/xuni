package com.jxx.xuni.group.query;

import com.jxx.xuni.group.dto.response.GroupPageApiResult;
import com.jxx.xuni.group.query.dynamic.GroupSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupQuery {
    Page<GroupPageApiResult.GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, Pageable pageable);
    List<GroupPageApiResult.GroupAllQueryResponse> readOwnWithFetch(Long groupMemberId);
}
