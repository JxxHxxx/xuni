package com.jxx.xuni.group.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupQuery {
    Page<GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, Pageable pageable);
    List<GroupAllQueryResponse> readOwnWithFetch(Long groupMemberId);
}
