package com.jobweb.job.controller;

import com.jobweb.job.domain.Resume;
import com.jobweb.job.domain.dto.request.ResumeCreationRequest;
import com.jobweb.job.domain.dto.request.ResumeUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.ResumeResponse;
import com.jobweb.job.service.ResumeService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @GetMapping
    public ApiResponse<List<ResumeResponse>> getAllResumes(
            @Filter Specification<Resume> spec,
            Pageable pageable){
        return ApiResponse.<List<ResumeResponse>>builder()
                .data(resumeService.getAllResumes(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ResumeResponse> getResumeById(
            @PathVariable("id") long resumeId){
        return ApiResponse.<ResumeResponse>builder()
                .data(resumeService.getResumeById(resumeId))
                .build();
    }

    @PostMapping
    public ApiResponse<ResumeResponse> createResume(
            @RequestBody ResumeCreationRequest request){
        return ApiResponse.<ResumeResponse>builder()
                .data(resumeService.createResume(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ResumeResponse> updateResume(
            @PathVariable("id") long resumeId,
            @RequestBody ResumeUpdateRequest request){
        return ApiResponse.<ResumeResponse>builder()
                .data(resumeService.updateResume(resumeId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteResume(
            @PathVariable("id") long resumeId){
        resumeService.deleteResume(resumeId);
        return ApiResponse.<Void>builder().build();
    }

}
