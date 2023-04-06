package com.jxx.xuni.group.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @DisplayName("그룹 엔티티 저장 및 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        Group group = TestGroupServiceSupporter.receiveSampleGroup(1l);
        //when
        Group saveGroup = groupRepository.save(group);

        //then
        assertThat(groupRepository.findById(saveGroup.getId())).isNotEmpty();
    }
}