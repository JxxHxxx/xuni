package com.xuni.api.auth.infra;

import com.xuni.core.auth.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginInfoEmail(String email);
}
