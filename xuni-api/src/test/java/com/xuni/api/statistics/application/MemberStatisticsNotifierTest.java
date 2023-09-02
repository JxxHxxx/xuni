package com.xuni.api.statistics.application;

import com.xuni.api.auth.infra.MemberRepository;
import com.xuni.api.group.TestGroupServiceSupporter;
import com.xuni.api.group.query.GroupReadRepository;
import com.xuni.api.statistics.infra.MemberStatisticsRepository;
import com.xuni.api.studyproduct.infra.StudyProductRepository;
import com.xuni.auth.domain.LoginInfo;
import com.xuni.auth.domain.Member;
import com.xuni.common.domain.Category;
import com.xuni.common.event.trigger.StatisticsAccessedEvent;
import com.xuni.group.domain.Group;
import com.xuni.statistics.domain.MemberStatistics;
import com.xuni.studyproduct.domain.StudyProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.xuni.auth.domain.AuthProvider.XUNI;

@Transactional // 영속성 컨택스트 종료 방지
@SpringBootTest
class MemberStatisticsNotifierTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GroupReadRepository groupReadRepository;
    @Autowired
    StudyProductRepository studyProductRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    MemberStatisticsRepository memberStatisticsRepository;

    @BeforeEach
    void beforeEach() {
        // 초기화
        groupReadRepository.deleteAll();
        memberStatisticsRepository.deleteAll();
        // member 생성
        Member member = new Member(LoginInfo.of("xuni@naver.com", "12341234"), "유니", XUNI);
        memberRepository.save(member);

        Member findMember = memberRepository.findByLoginInfoEmail("xuni@naver.com").get();
        Long mid = findMember.getId();

        // study 상품 생성
        StudyProduct studyProduct = StudyProduct.builder()
                .category(Category.JAVA)
                .thumbnail("image")
                .name("자바의 정석")
                .creator("남궁성").build();

        StudyProduct product = studyProductRepository.save(studyProduct);
        String spid = product.getId();

        // 시작 그룹 생성
        Group group = TestGroupServiceSupporter.startGroup(spid, mid);
        Group savedGroup = groupReadRepository.save(group);
        groupId = savedGroup.getId();

        studyProductId = spid;
        memberId = mid;
    }

    String studyProductId = null;
    Long memberId = null;
    Long groupId = null;

    @DisplayName("MemberStatistics 가 존재하지 않을 경우 StatisticsUpdateEven t를 발생하면" +
            "사용자의 MemberStatistics 이 생성되고 progress 가 업데이트 된다.")
    @Test
    void handle_statistics_update_event() {
        //given
        Optional<MemberStatistics> optionalStatistics = memberStatisticsRepository.readBy(memberId, studyProductId);
        Assertions.assertThat(optionalStatistics).isEmpty();

        Group group = groupReadRepository.readOneWithFetch(groupId).get();
        group.doTask(1l, memberId);
        group.doTask(2l, memberId);
        group.doTask(3l, memberId);
        //when
        StatisticsAccessedEvent event = new StatisticsAccessedEvent(memberId, studyProductId);
        eventPublisher.publishEvent(event);

        MemberStatistics updateStatistics = memberStatisticsRepository.readBy(memberId, studyProductId).get();
        //then
        Assertions.assertThat(updateStatistics.getProgress()).isEqualTo(100);
    }
}