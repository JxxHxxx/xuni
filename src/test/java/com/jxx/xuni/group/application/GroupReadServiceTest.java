package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.group.dto.response.GroupReadOneResponse;
import com.jxx.xuni.studyproduct.domain.Category;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.jxx.xuni.studyproduct.domain.Category.SPRING_FRAMEWORK;

@ServiceTest
class GroupReadServiceTest extends ServiceCommon {

    @Autowired
    GroupReadService groupReadService;
    @Autowired
    GroupRepository groupRepository;
    Long savedGroupId;

    @BeforeEach
    void beforeEach() {
        Group group1 = TestGroupServiceSupporter.receiveSampleGroup(Category.JAVA);
        Group group2 = TestGroupServiceSupporter.receiveSampleGroup(SPRING_FRAMEWORK);
        Group group3 = TestGroupServiceSupporter.receiveSampleGroup(SPRING_FRAMEWORK);

        Group savedGroup = groupRepository.save(group1);
        savedGroupId = savedGroup.getId();
        groupRepository.save(group2);
        groupRepository.save(group3);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }


    @DisplayName("그룹 전체 조회 성공 케이스 현재 존재하는 모든 그룹을 가져온다.")
    @Test
    void read_all() {
        //when
        List<GroupReadAllResponse> responses = groupReadService.readAll();
        //then
        int totalGroupSize = 3;
        Assertions.assertThat(responses.size()).isEqualTo(totalGroupSize);
    }

    @DisplayName("그룹 조회 성공 케이스 선택한 단 한개의 그룹만 가져온다.")
    @Test
    void read_one() {
        //when
        GroupReadOneResponse response = groupReadService.readOne(savedGroupId);
        //then
        Assertions.assertThat(response.getGroupId()).isEqualTo(savedGroupId);
    }

    @DisplayName("그룹 카테고리 조회 성공 지정한 카테고리의 그룹만 가져온다.")
    @Test
    void read_by_category() {
        //when
        List<GroupReadAllResponse> responses = groupReadService.readByCategory(SPRING_FRAMEWORK);
        //then
        int springFrameworkSize = 2;
        Assertions.assertThat(responses.size()).isEqualTo(springFrameworkSize);
    }
}