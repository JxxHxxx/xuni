package com.xuni.api.group.application;

import com.xuni.api.group.dto.response.GroupMemberDto;
import com.xuni.api.group.dto.response.GroupReadAllResponse;
import com.xuni.api.group.dto.response.GroupReadOneResponse;
import com.xuni.api.group.dto.response.GroupStudyCheckResponse;
import com.xuni.common.exception.CommonExceptionMessage;
import com.xuni.common.query.ModifiedPagingForm;
import com.xuni.common.query.PagingModifier;
import com.xuni.group.domain.Group;
import com.xuni.group.domain.Task;
import com.xuni.api.group.query.GroupAllQueryResponse;
import com.xuni.api.group.query.GroupReadRepository;
import com.xuni.api.group.query.dynamic.GroupSearchCondition;
import com.xuni.common.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupReadService {

    private final GroupReadRepository groupReadRepository;
    private final PagingModifier pagingModifier;

    public List<GroupReadAllResponse> readAll() {
        List<Group> groups = groupReadRepository.readAllWithFetch();

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod())).toList();
    }

    @Transactional
    public GroupReadOneResponse readOne(Long groupId, Long userId) {
        Group group = groupReadRepository.readOneWithFetch(groupId).get();
        group.updateGroupMemberLastVisitedTime(userId);

        List<GroupMemberDto> groupMembers = group.getGroupMembers().stream()
                .map(groupMember -> new GroupMemberDto(
                        groupMember.getGroupMemberId(),
                        groupMember.getGroupMemberName(),
                        groupMember.getIsLeft(),
                        groupMember.getLastVisitedTime()))
                .toList();

        return new GroupReadOneResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod(),
                groupMembers);
    }

    public List<GroupReadAllResponse> readByCategory(Category category) {
        List<Group> groups = groupReadRepository.readByCategoryWithFetch(category);

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod())).toList();
    }

    public Page<GroupAllQueryResponse> searchGroup(GroupSearchCondition condition, int page, int size) {
        condition.nullHandle();

        ModifiedPagingForm form = pagingModifier.modify(page, size);
        return groupReadRepository.searchGroup(condition, PageRequest.of(form.page(), form.size()));
    }

    public List<GroupAllQueryResponse> readOwn(Long groupMemberId) {
        return groupReadRepository.readOwnWithFetch(groupMemberId);
    }

    public List<GroupStudyCheckResponse> readGroupTask(Long groupId, Long userId) {
        Group group = groupReadRepository.readTaskWithFetch(groupId, userId)
                .orElseThrow(() -> new IllegalArgumentException(CommonExceptionMessage.BAD_REQUEST));
        List<Task> tasks = group.receiveTasksOf(userId);

        return tasks.stream().map(task -> new GroupStudyCheckResponse(
                task.getChapterId(),
                task.getTitle(),
                task.isDone())).toList();
    }
}
