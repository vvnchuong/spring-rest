package com.jobweb.job.mapper;

import com.jobweb.job.domain.Job;
import com.jobweb.job.domain.dto.request.JobCreationRequest;
import com.jobweb.job.domain.dto.request.JobUpdateRequest;
import com.jobweb.job.domain.dto.response.JobResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobMapper {

    Job toJob(JobCreationRequest request);

    default JobResponse toJobResponse(Job job) {
        if(job == null)
            return null;

        List<JobResponse.SkillJob> skillJob = null;
        if(job.getSkills() != null){
            skillJob = job.getSkills().stream()
                    .map(skill -> JobResponse.SkillJob.builder()
                            .id(skill.getId())
                            .build())
                    .toList();
        }

        return JobResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .level(job.getLevel())
                .description(job.getDescription())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .active(job.isActive())
                .createdAt(job.getCreatedAt())
                .createdBy(job.getCreatedBy())
                .updatedAt(job.getUpdatedAt())
                .updatedBy(job.getUpdatedBy())
                .skills(skillJob)
                .build();
    }

    void updateJob(@MappingTarget Job job, JobUpdateRequest request);

}
