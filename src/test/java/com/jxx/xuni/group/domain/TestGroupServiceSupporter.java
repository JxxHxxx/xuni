package com.jxx.xuni.group.domain;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.group.dto.request.GroupTaskForm;
import com.jxx.xuni.common.domain.Category;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.jxx.xuni.group.domain.GroupStatus.*;
import static com.jxx.xuni.auth.domain.Authority.ADMIN;
import static com.jxx.xuni.auth.domain.Authority.USER;

// TODO : 원활한 테스트를 위해 꼭 필요한 클래스인 만큼 필요한 케이스 별로 잘 정리해서 작성하자. Refactoring 필요

public class TestGroupServiceSupporter {

    public static Group receiveSampleGroup(Long hostId) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID","자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
    }

    public static Group receiveSampleGroup(Long hostId, Category category) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID","자바의 정석", category),
                new Host(hostId, "재헌"));
    }

    public static Group receiveSampleGroup(Category category) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("UUID","Real MySQL", category),
                new Host(1l, "재헌"));
    }

    public static Group receiveSampleGroup(Long hostId, Integer capacity) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(capacity),
                Study.of("UUID","자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
    }

    public static Group receiveEndGroup(Category category) {
        Group group = receiveSampleGroup(category);
        group.changeGroupStatusTo(END);

        return group;
    }

    public static Group receiveGatherCompleteGroup(Category category) {
        Group group = receiveSampleGroup(category);
        group.changeGroupStatusTo(GATHER_COMPLETE);

        return group;
    }

    public static Group receiveStartGroup(Category category) {
        Group group = receiveSampleGroup(category);
        group.changeGroupStatusTo(START);

        return group;
    }

    public static SimpleMemberDetails receiveSampleMemberDetails(Long memberId) {
        return new SimpleMemberDetails(memberId, "leesin5498@naver.com", "재헌");
    }

    public static SimpleMemberDetails AdminMemberDetails(Long memberId) {
        return new SimpleMemberDetails(memberId, "jsmtmt@naver.com", "유니", ADMIN);
    }

    public static SimpleMemberDetails UserMemberDetails(Long memberId) {
        return new SimpleMemberDetails(memberId, "leesin5498@naver.com", "재헌", USER);
    }

    public static List<GroupTaskForm> studyCheckForms = List.of(
            new GroupTaskForm(1l, "객체"),
            new GroupTaskForm(2l, "타입"),
            new GroupTaskForm(3l, "인터페이스"));

    public static Group startedGroupSample(Long hostId, Integer capacity) {
        Group group = new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(capacity),
                Study.of("UUID", "자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));

        group.join(new GroupMember(2l, "지니", group));
        group.changeGroupStatusTo(GATHER_COMPLETE);
        group.start(hostId, studyCheckForms);
        return group;
    }

    public static Group startGroup(String studyProductId, Long hostId) {
        Group group = new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of(studyProductId, "자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));

        group.join(new GroupMember(2l, "지니", group));
        group.changeGroupStatusTo(GATHER_COMPLETE);
        group.start(hostId, studyCheckForms);
        return group;
    }
}
