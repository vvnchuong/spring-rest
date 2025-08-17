package com.jobweb.job.controller;

import com.jobweb.job.domain.Job;
import com.jobweb.job.domain.dto.request.JobCreationRequest;
import com.jobweb.job.domain.dto.request.JobUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.JobResponse;
import com.jobweb.job.service.JobService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService){
        this.jobService = jobService;
    }

    @GetMapping
    public ApiResponse<List<JobResponse>> getAllJobs(
            @Filter Specification<Job> spec, Pageable pageable){
        return ApiResponse.<List<JobResponse>>builder()
                .data(jobService.getAllJobs(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJobById(
            @PathVariable("id") long jobId){
        return ApiResponse.<JobResponse>builder()
                .data(jobService.getJobById(jobId))
                .build();
    }

    @PostMapping
    public ApiResponse<JobResponse> createJob(
            @RequestBody JobCreationRequest request){
        return ApiResponse.<JobResponse>builder()
                .data(jobService.createJob(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<JobResponse> updateJob(
            @PathVariable("id") long jobId,
            @RequestBody JobUpdateRequest request){
        return ApiResponse.<JobResponse>builder()
                .data(jobService.updateJob(jobId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(
            @PathVariable("id") long jobId){
        jobService.deleteJob(jobId);
        return ApiResponse.<Void>builder().build();
    }

}
