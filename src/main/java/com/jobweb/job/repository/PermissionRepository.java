package com.jobweb.job.repository;

import com.jobweb.job.domain.Permission;
import com.jobweb.job.domain.dto.request.PermissionCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>,
        JpaSpecificationExecutor<Permission> {

    @Query("""
            SELECT p FROM Permission p
            WHERE p.name = :#{#req.name}
            AND p.apiPath = :#{#req.apiPath}
            AND p.method = :#{#req.method}
            AND p.module = :#{#req.module}
            """)
    Optional<Permission> existsConflict(@Param("req") PermissionCreationRequest request);

    List<Permission> findByIdIn(List<Long> id);

}
