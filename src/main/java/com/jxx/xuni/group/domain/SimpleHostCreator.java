package com.jxx.xuni.group.domain;

import com.jxx.xuni.member.domain.Member;
import com.jxx.xuni.member.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleHostCreator {

    private final MemberRepository memberRepository;

    public Host createHost(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버"));
        return new Host(findMember.getId(), findMember.getName());
    }
}
