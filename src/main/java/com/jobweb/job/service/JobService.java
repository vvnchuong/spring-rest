package com.jobweb.job.service;

import com.jobweb.job.domain.Job;
import com.jobweb.job.domain.Skill;
import com.jobweb.job.domain.dto.request.JobCreationRequest;
import com.jobweb.job.domain.dto.request.JobUpdateRequest;
import com.jobweb.job.domain.dto.response.JobResponse;
import com.jobweb.job.mapper.JobMapper;
import com.jobweb.job.repository.JobRepository;
import com.jobweb.job.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;

    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository,
                      SkillRepository skillRepository,
                      JobMapper jobMapper){
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.jobMapper = jobMapper;
    }

    public Page<JobResponse> getAllJobs(Specification<Job> spec, Pageable pageable){
        return jobRepository.findAll(spec, pageable)
                .map(jobMapper::toJobResponse);
    }

    public JobResponse getJobById(long jobId){
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()){
            throw new RuntimeException("job not existed");
        }
        return jobMapper.toJobResponse(job.get());
    }

    public JobResponse createJob(JobCreationRequest request){
        List<Long> skillIds = request.getSkills()
                .stream().map(JobCreationRequest.SkillJob::getId)
                .toList();

        List<Skill> skill = skillRepository.findByIdIn(skillIds);
        if(skill.isEmpty())
            throw new RuntimeException("Skill not found");

        Job job = jobMapper.toJob(request);

        String email = getEmailInToken();
        job.setCreatedBy(email);
        job.setCreatedAt(Instant.now());

        return jobMapper.toJobResponse(jobRepository.save(job));
    }

    public JobResponse updateJob(long jobId, JobUpdateRequest request){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        jobMapper.updateJob(job, request);

        String email = getEmailInToken();
        job.setUpdatedBy(email);
        job.setUpdatedAt(Instant.now());

        return jobMapper.toJobResponse(jobRepository.save(job));
    }

    public void deleteJob(long jobId){
        jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        jobRepository.deleteById(jobId);
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
