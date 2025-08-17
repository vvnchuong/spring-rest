package com.jobweb.job.mapper;

import com.jobweb.job.domain.Skill;
import com.jobweb.job.domain.dto.request.SkillCreationRequest;
import com.jobweb.job.domain.dto.request.SkillUpdateRequest;
import com.jobweb.job.domain.dto.response.SkillResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    Skill toSkill(SkillCreationRequest request);

    SkillResponse toSkillResponse(Skill skill);

    void updateSkill(@MappingTarget Skill skill, SkillUpdateRequest request);

}
