package com.jobweb.job.service;

import com.jobweb.job.domain.Job;
import com.jobweb.job.domain.Skill;
import com.jobweb.job.domain.dto.request.SkillCreationRequest;
import com.jobweb.job.domain.dto.request.SkillUpdateRequest;
import com.jobweb.job.domain.dto.response.SkillResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.SkillMapper;
import com.jobweb.job.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository,
                        SkillMapper skillMapper){
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    public Page<SkillResponse> getAllSkills(
            Specification<Skill> spec, Pageable pageable){
        return skillRepository.findAll(spec, pageable)
                .map(skillMapper::toSkillResponse);
    }

    public SkillResponse getSkillById(long skillId){
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_EXISTED));
        return skillMapper.toSkillResponse(skill);
    }

    public SkillResponse createSkill(SkillCreationRequest request){
        Optional<Skill> skill = skillRepository.findByName(request.getName());
        if(skill.isPresent())
            throw new AppException(ErrorCode.SKILL_EXISTED);

        Skill newSkill = skillMapper.toSkill(request);

        String email = getEmailInToken();
        newSkill.setCreatedBy(email);
        newSkill.setCreatedAt(Instant.now());

        return skillMapper.toSkillResponse(skillRepository.save(newSkill));
    }

    public SkillResponse updateSkill(long skillId, SkillUpdateRequest request){
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new AppException(ErrorCode.SKILL_NOT_EXISTED));
        skillMapper.updateSkill(skill, request);

        String email = getEmailInToken();
        skill.setUpdatedBy(email);
        skill.setUpdatedAt(Instant.now());

        return skillMapper.toSkillResponse(skillRepository.save(skill));
    }

    private String getEmailInToken(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof JwtAuthenticationToken jwtAuth)){
            throw new RuntimeException("Invalid Token");
        } else {
            return jwtAuth.getToken().getClaimAsString("iss");
        }
    }

}
