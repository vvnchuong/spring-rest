package com.jobweb.job.controller;

import com.jobweb.job.domain.Skill;
import com.jobweb.job.domain.dto.request.SkillCreationRequest;
import com.jobweb.job.domain.dto.request.SkillUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.SkillResponse;
import com.jobweb.job.service.SkillService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService){
        this.skillService = skillService;
    }

    @GetMapping
    public ApiResponse<List<SkillResponse>> getAllSkills(
            @Filter Specification<Skill> spec, Pageable pageable){
        return ApiResponse.<List<SkillResponse>>builder()
                .data(skillService.getAllSkills(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SkillResponse> getSkillById(
            @PathVariable("id") long skillId){
        return ApiResponse.<SkillResponse>builder()
                .data(skillService.getSkillById(skillId))
                .build();
    }

    @PostMapping
    public ApiResponse<SkillResponse> createSkill(
            @RequestBody SkillCreationRequest request){
        return ApiResponse.<SkillResponse>builder()
                .data(skillService.createSkill(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SkillResponse> updateSkill(
            @PathVariable("id") long skillId,
            @RequestBody SkillUpdateRequest request){
        return ApiResponse.<SkillResponse>builder()
                .data(skillService.updateSkill(skillId, request))
                .build();
    }

}
