package com.jxx.xuni.group.application;

import com.jxx.xuni.group.domain.*;
import com.jxx.xuni.group.dto.response.GroupPageApiResult;
import com.jxx.xuni.group.dto.response.GroupReadAllResponse;
import com.jxx.xuni.group.dto.response.GroupReadOneResponse;
import com.jxx.xuni.group.dto.response.GroupStudyCheckResponse;
import com.jxx.xuni.support.ServiceCommon;
import com.jxx.xuni.support.ServiceTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.studyproduct.domain.Category.*;
import static com.jxx.xuni.studyproduct.domain.Category.SPRING_FRAMEWORK;
import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class GroupReadServiceTest extends ServiceCommon {

    @Autowired
    GroupReadService groupReadService;
    @Autowired
    GroupRepository groupRepository;
    Long savedGroupId;

    @BeforeEach
    void beforeEach() {
        Group group1 = TestGroupServiceSupporter.receiveSampleGroup(JAVA);
        Group group2 = TestGroupServiceSupporter.receiveSampleGroup(SPRING_FRAMEWORK);
        Group group3 = TestGroupServiceSupporter.receiveSampleGroup(SPRING_FRAMEWORK);

        Group savedGroup = groupRepository.save(group1);
        savedGroupId = savedGroup.getId();
        groupRepository.saveAll(List.of(group2, group3));
    }

    @AfterEach
    void afterEach() {
        groupRepository.deleteAll();
    }


    @DisplayName("그룹 전체 조회 성공 케이스 " +
            "현재 존재하는 모든 그룹 중 GroupStatus 가 END 경우를 제외하고 가져온다.")
    @Test
    void read_all() {
        //given
        Group endedGroup = TestGroupServiceSupporter.receiveEndGroup(JAVA);
        groupRepository.save(endedGroup);
        //when
        List<GroupReadAllResponse> responses = groupReadService.readAll();
        //then
        int totalGroupSize = 3;
        assertThat(responses.size()).isEqualTo(totalGroupSize);

        List<GroupStatus> groupStatuses = responses.stream().map(r -> r.getGroupStatus()).toList();
        assertThat(groupStatuses).doesNotContain(END);
    }

    @DisplayName("그룹 조회 성공 케이스 선택한 단 한개의 그룹만 가져온다.")
    @Test
    void read_one() {
        //when
        Long anyUserId = 1l;
        GroupReadOneResponse response = groupReadService.readOne(savedGroupId, anyUserId);
        //then
        assertThat(response.getGroupId()).isEqualTo(savedGroupId);
    }

    @DisplayName("그룹 카테고리 조회 성공 " +
            "GROUPSTATUS가 END인 경우를 제외하고 " +
            "지정한 카테고리의 그룹만 가져온다.")
    @Test
    void read_by_category() {
        //given
        Group endedGroup = TestGroupServiceSupporter.receiveEndGroup(SPRING_FRAMEWORK);
        groupRepository.save(endedGroup);
        //when
        List<GroupReadAllResponse> responses = groupReadService.readByCategory(SPRING_FRAMEWORK);
        //then
        int totalGroupSize = 2;
        assertThat(responses.size()).isEqualTo(totalGroupSize);
    }

    @DisplayName("그룹 스터디 체크 조회 성공" +
            "1. chapterId 기준으로 정렬된 상태로 나와야 한다.")
    @Test
    void read_study_check() {
        //given
        Group startGroup = TestGroupServiceSupporter.startedGroupSample(1l,5);
        Group group = groupRepository.save(startGroup);
        Long groupId = group.getId();
        //when
        List<GroupStudyCheckResponse> response = groupReadService.readStudyCheck(groupId, 1l);
        //then
        assertThat(response.get(0).getChapterId()).isLessThan(response.get(1).getChapterId());
        assertThat(response.get(1).getChapterId()).isLessThan(response.get(2).getChapterId());
    }

    @DisplayName("자신이 소속되어 있는 그룹 조회 규칙 - 떠난 그룹은 조회되지 않는다.")
    @Test
    void read_own_roles_1_not_read_left_group() {
        //given
        Group javaGroup = TestGroupServiceSupporter.receiveSampleGroup(10l, JAVA);
        javaGroup.join(new GroupMember(2l, "유니", javaGroup));

        Group savedJavaGroup = groupRepository.save(javaGroup);
        savedJavaGroup.leave(2l);
        //when
        List<GroupPageApiResult.GroupAllQueryResponse> response = groupReadService.readOwn(2l);
        //then
        assertThat(response).isEmpty();
    }

    @DisplayName("자신이 소속되어 있는 그룹 조회 규칙 - 자신이 소속된 그룹만 조회된다.")
    @Test
    void read_own_roles_2_belong_only_self() {
        //given
        Group javaGroup = TestGroupServiceSupporter.receiveSampleGroup(10l, JAVA);
        Group springGroup = TestGroupServiceSupporter.receiveSampleGroup(5l, SPRING_FRAMEWORK);
        javaGroup.join(new GroupMember(2l, "유니", javaGroup));

        groupRepository.saveAll(List.of(javaGroup, springGroup));
        //when
        List<GroupPageApiResult.GroupAllQueryResponse> response = groupReadService.readOwn(2l);
        //then
        assertThat(response).extracting("study.category").containsOnly(JAVA);
    }

    @DisplayName("자신이 소속되어 있는 그룹 조회 규칙 - 마지막으로 방문한 순으로 정렬된다.")
    @Test
    void read_own_roles_3_sort_by_last_visited_time() throws InterruptedException {
        //given
        Group javaGroup = TestGroupServiceSupporter.receiveSampleGroup(5l, JAVA);
        Thread.sleep(100);
        Group springGroup = TestGroupServiceSupporter.receiveSampleGroup(5l, SPRING_FRAMEWORK);
        Thread.sleep(100);
        Group reactGroup = TestGroupServiceSupporter.receiveSampleGroup(5l, REACT);
        // 가입(join) 시, 마지막 방문 시간이 최초로 업데이트 된다.
        javaGroup.join(new GroupMember(2l, "유니", javaGroup));
        Thread.sleep(100);
        springGroup.join(new GroupMember(2l, "유니", springGroup));
        Thread.sleep(100);
        reactGroup.join(new GroupMember(2l, "유니", reactGroup));

        groupRepository.saveAll(List.of(javaGroup, springGroup, reactGroup));
        //when
        List<GroupPageApiResult.GroupAllQueryResponse> response = groupReadService.readOwn(2l);

        for (GroupPageApiResult.GroupAllQueryResponse q : response) {
            System.out.println("category : " + q.getStudy().getCategory());
        }
        //then
        assertThat(response).extracting("study.category").containsExactly(REACT, SPRING_FRAMEWORK, JAVA);
    }
}