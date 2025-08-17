package com.jobweb.job.repository;

import com.jobweb.job.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    Optional<Skill> findByName(String name);

    List<Skill> findByIdIn(List<Long> id);

}
