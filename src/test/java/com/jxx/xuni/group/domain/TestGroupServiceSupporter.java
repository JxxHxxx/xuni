package com.jxx.xuni.group.domain;

import com.jxx.xuni.auth.application.SimpleMemberDetails;
import com.jxx.xuni.studyproduct.domain.Category;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.jxx.xuni.member.domain.Authority.ADMIN;
import static com.jxx.xuni.member.domain.Authority.USER;

public class TestGroupServiceSupporter {

    public static Group receiveSampleGroup(Long hostId) {
        return new Group(Period.of(LocalDate.now(), LocalDate.of(2023, 12, 31)),
                Time.of(LocalTime.MIDNIGHT, LocalTime.NOON),
                new Capacity(5),
                Study.of("자바의 정석", Category.JAVA),
                new Host(hostId, "재헌"));
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
}
