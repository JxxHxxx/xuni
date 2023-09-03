package com.xuni.api.group.query;

import com.xuni.api.group.TestGroupServiceSupporter;
import com.xuni.api.group.infra.GroupRepository;
import com.xuni.api.group.query.dynamic.GroupSearchCondition;
import com.xuni.core.group.domain.Group;
import com.xuni.core.group.domain.Study;
import com.xuni.core.common.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.xuni.core.common.domain.Category.*;
import static com.xuni.core.group.domain.GroupStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(value = PER_CLASS)
@Transactional
class GroupQueryImplTest {

    @Autowired
    GroupReadRepository groupReadRepository;
    @Autowired
    GroupRepository groupRepository;

    /**
     * beforeEach 사이클에서 데이터는 다음과 같이 들어간다.
     * Category : JAVA / GroupStatus : GATHERING = 10개
     * Category : JAVA / GroupStatus : GATHER_COMPLETE = 10개
     * Category : MYSQL / GroupStatus : START = 10개
     * Category : JAVA / GroupStatus : END = 10개
     * Category : REACT / GroupStatus : GATHERING = 10개
     */

    @BeforeAll
    void beforeAll() {
        for (long i = 0; i < 10; i++) {
            Group javaGatheringGroup = TestGroupServiceSupporter.receiveSampleGroup(JAVA);
            Group javaGatherCompleteGroup = TestGroupServiceSupporter.receiveGatherCompleteGroup(JAVA);
            Group mysqlStartGroup = TestGroupServiceSupporter.receiveStartGroup(MYSQL);
            Group javaEndGroup = TestGroupServiceSupporter.receiveEndGroup(JAVA);
            Group reactGatheringGroup = TestGroupServiceSupporter.receiveSampleGroup(REACT);

            groupRepository.saveAll(List.of(
                    javaGatheringGroup,
                    javaGatherCompleteGroup,
                    mysqlStartGroup,
                    javaEndGroup,
                    reactGatheringGroup));
        }
    }

    @DisplayName("기본 검색 조건을 검증한다 " +
            "1. GroupStatus는 GATHERING,GATHER_COMPLETE, START 여야만 한다. " +
            "2. Category는 모든 종류가 올 수 있다.")
    @ParameterizedTest(name = "케이스 : {0}")
    @CsvSource(value = {"null 값으로 인한 기본 조건, null, null",
                        "empty value로 인한 기본 조건, null, empty"},
                nullValues = "null", emptyValue = "empty")
    void search_group_no_set_any_condition(String description, Category category, String readType) {
        //given
        GroupSearchCondition condition = new GroupSearchCondition(category, readType, false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        Page<GroupAllQueryResponse> response = groupReadRepository.searchGroup(condition, pageable);
        List<GroupAllQueryResponse> content = response.getContent();
        //then
        Assertions.assertThat(content).extracting("groupStatus").containsOnly(GATHERING, GATHER_COMPLETE, START);

        List<Study> studies = content.stream().map(c -> c.getStudy()).toList();
        assertThat(studies).extracting("category").contains(JAVA, MYSQL, REACT);
    }

    @DisplayName("카테고리 조건을 검증한다. 카테고리 조건 설정 시, 설정한 카테고리 그룹만 조회된다.")
    @Test
    void search_group_category_condition() {
        //given
        GroupSearchCondition condition = new GroupSearchCondition(MYSQL, null, false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then
        List<Study> studies = content.stream().map(c -> c.getStudy()).toList();
        assertThat(studies).extracting("category").containsOnly(MYSQL);
    }


    @DisplayName("읽기 타입 조건을 검증한다. all 일 경우, GroupStatus = GATHERING, GATHER_COMPLETE, START, END 모두 조회한다.")
    @Test
    void search_group_read_type_condition_all() {
        GroupSearchCondition condition = new GroupSearchCondition(null, "all", false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then
        Assertions.assertThat(content).extracting("groupStatus").containsOnly(GATHERING, GATHER_COMPLETE, START, END);
    }

    @DisplayName("읽기 타입 조건을 검증한다. gathering 일 경우, GroupStatus = GATHERING 만 조회한다.")
    @Test
    void search_group_read_type_condition_gathering() {
        GroupSearchCondition condition = new GroupSearchCondition(null, "gathering", false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then
        Assertions.assertThat(content).extracting("groupStatus").containsOnly(GATHERING);
    }

    @DisplayName("읽기 타입 조건을 검증한다. default 일 경우, GroupStatus = GATHERING, GATHER_COMPLETE, START 만 조회한다.")
    @Test
    void search_group_read_type_condition_default() {
        GroupSearchCondition condition = new GroupSearchCondition(null, "default", false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then
        Assertions.assertThat(content).extracting("groupStatus").containsOnly(GATHERING, GATHER_COMPLETE, START);
    }

    @DisplayName("읽기 타입 조건을 검증한다. 그 외 값이 들어올 경우 GroupStatus = GATHERING, GATHER_COMPLETE, START 만 조회한다.")
    @Test
    void search_group_read_type_condition_other_value() {
        GroupSearchCondition condition = new GroupSearchCondition(null, "otherValue", false, null, null);
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then
        Assertions.assertThat(content).extracting("groupStatus").containsOnly(GATHERING, GATHER_COMPLETE, START);
    }

    @DisplayName("입력어 조건을 검증한다. 입력어가 포함(일치가 아니다.)되어 있는 그룹만 조회한다.")
    @Test
    void search_group_subject_condition() {
        GroupSearchCondition condition = new GroupSearchCondition(null, null, false, null, "MySQL");
        PageRequest pageable = PageRequest.of(0, 20);
        //when
        List<GroupAllQueryResponse> content = groupReadRepository.searchGroup(condition, pageable).getContent();
        //then

        Assertions.assertThat(content).extracting("study.subject").containsOnly("Real MySQL");

    }
}