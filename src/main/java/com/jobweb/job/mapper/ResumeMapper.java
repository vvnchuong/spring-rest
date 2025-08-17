package com.jobweb.job.mapper;

import com.jobweb.job.domain.Resume;
import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.ResumeCreationRequest;
import com.jobweb.job.domain.dto.request.ResumeUpdateRequest;
import com.jobweb.job.domain.dto.response.ResumeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "job", source = "job")
    Resume toResume(ResumeCreationRequest request);

    default ResumeResponse toResumeResponse(Resume resume){
        if(resume == null)
            return null;

        ResumeResponse.UserResume userResume = null;
        if(resume.getUser() != null){
            userResume = ResumeResponse.UserResume.builder()
                    .id(resume.getUser().getId())
                    .name(resume.getUser().getName())
                    .build();
        }

        ResumeResponse.JobResume jobResume = null;
        if(resume.getJob() != null){
            jobResume = ResumeResponse.JobResume.builder()
                    .id(resume.getJob().getId())
                    .name(resume.getJob().getName())
                    .build();
        }

        return ResumeResponse.builder()
                .id(resume.getId())
                .email(resume.getEmail())
                .url(resume.getUrl())
                .status(resume.getStatus().name())
                .createdAt(resume.getCreatedAt())
                .createdBy(resume.getCreatedBy())
                .updatedAt(resume.getUpdatedAt())
                .updatedBy(resume.getUpdatedBy())
                .user(userResume)
                .job(jobResume)
                .build();
    }

    void updateResume(@MappingTarget Resume resume, ResumeUpdateRequest request);

}
