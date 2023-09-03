package com.xuni.api.auth.infra;

import com.xuni.core.auth.domain.AuthCode;
import com.xuni.core.auth.domain.UsageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
    Optional<AuthCode> findByEmailAndUsageType(String email, UsageType usageType);
}