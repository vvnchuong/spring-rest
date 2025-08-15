package com.jobweb.job.repository;

import com.jobweb.job.domain.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {
}
