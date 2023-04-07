package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.Group;
import com.jxx.xuni.group.domain.GroupRepository;
import com.jxx.xuni.group.domain.TestGroupServiceSupporter;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ServiceTest
class GroupReadServiceTest {

    @Autowired
    GroupReadService groupReadService;

    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    void beforeEach() {
        Group group1 = TestGroupServiceSupporter.receiveSampleGroup(1l);
        Group group2 = TestGroupServiceSupporter.receiveSampleGroup(2l);

        groupRepository.save(group1);
        groupRepository.save(group2);
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }


    @DisplayName("서비스 레이어 그룹 조회 성공 케이스 - 현재 존재하는 그룹 갯수만큼 반환한다.")
    @Test
    void read_all() {
        //when
        List<GroupReadAllResponse> responses = groupReadService.readAll();
        //then
        Assertions.assertThat(responses.size()).isEqualTo(2);
    }
}