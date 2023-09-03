package com.xuni.api.group.application;

import com.xuni.api.auth.infra.MemberRepository;
import com.xuni.core.auth.domain.Member;
import com.xuni.core.group.domain.Host;
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
