package com.xuni.api.group.acceptance;

import com.xuni.auth.application.MemberDetails;
import com.xuni.auth.application.SimpleMemberDetails;
import com.xuni.group.application.GroupJoinFacade;
import com.xuni.group.application.GroupManagingService;
import com.xuni.group.domain.Group;
import com.xuni.group.domain.GroupRepository;
import com.xuni.group.domain.TestGroupServiceSupporter;
import com.xuni.auth.domain.LoginInfo;
import com.xuni.auth.domain.Member;
import com.xuni.auth.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 스터디 그룹 가입은 낙관적 락을 체택합니다.
 */

@SpringBootTest
public class GroupJoinConcurrencyTest {

    @Autowired
    GroupManagingService groupManagingService;
    @Autowired
    GroupJoinFacade groupJoinFacade;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        List<Member> members = new ArrayList<>();
        for (long i = 1; i <= 20l; i++) {
            Member member = Member.builder()
                    .name("jamong")
                    .id(i)
                    .loginInfo(LoginInfo.of("dummy@google.com", "12341234"))
                    .build();
            members.add(member);
        }

        memberRepository.saveAll(members);

        Group group = TestGroupServiceSupporter.receiveSampleGroup(20l, 20);
        groupRepository.save(group);
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @DisplayName("클라이언트 다수가 동시에 스터디 그룹 입장을 요청했을 때, " +
            "요청한 클라이언트 수 만큼 group 엔티티 left_capacity 필드가 감소해야 한다. " +
            "그룹 입장 가능 인원 이상의 클라이언트가 요청할 경우에도 left_capacity 는 0 미만으로 감소할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"15, 4", "25, 0"})
    void client_request_join_with_concurrency(int threadCount, int expectedLeftCapacity) throws InterruptedException {
        //given
        Group group = groupRepository.findAll().get(0);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 1; i <= threadCount; i++) {
            MemberDetails memberDetails = new SimpleMemberDetails(Long.valueOf(i), "dummy@google.com", "jamong");
            executorService.submit(() -> {
                try {
                    groupJoinFacade.join(memberDetails, group.getId());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        //then
        Group updateGroup = groupRepository.findById(group.getId()).orElseThrow();
        Assertions.assertThat(updateGroup.getCapacity().getLeftCapacity()).isEqualTo(expectedLeftCapacity);
    }
}
