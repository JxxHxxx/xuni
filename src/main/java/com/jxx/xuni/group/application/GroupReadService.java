package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.dto.response.GroupReadOneResponse;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.group.query.GroupReadRepository;
import com.jxx.xuni.studyproduct.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupReadService {

    private final GroupReadRepository groupReadRepository;

    public List<GroupReadAllResponse> readAll() {
        List<Group> groups = groupReadRepository.readAllWithFetch();

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getTime(),
                group.getPeriod(),
                group.getGroupMembers())).collect(Collectors.toList());
    }

    public GroupReadOneResponse readOne(Long groupId) {
        Group group = groupReadRepository.findById(groupId).get();

        return new GroupReadOneResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getStudy(),
                group.getTime(),
                group.getPeriod(),
                group.getGroupMembers());
    }

    public List<GroupReadAllResponse> readByCategory(Category category) {
        List<Group> groups = groupReadRepository.readByCategoryWithFetch(category);

        return groups.stream().map(group -> new GroupReadAllResponse(
                group.getId(),
                group.getCapacity(),
                group.getGroupStatus(),
                group.getHost(),
                group.getTime(),
                group.getPeriod(),
                group.getGroupMembers())).collect(Collectors.toList());
    }
}
