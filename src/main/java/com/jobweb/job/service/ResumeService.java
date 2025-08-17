package com.jobweb.job.service;

import com.jobweb.job.domain.Job;
import com.jobweb.job.domain.Resume;
import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.ResumeCreationRequest;
import com.jobweb.job.domain.dto.request.ResumeUpdateRequest;
import com.jobweb.job.domain.dto.response.ResumeResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.ResumeMapper;
import com.jobweb.job.repository.JobRepository;
import com.jobweb.job.repository.ResumeRepository;
import com.jobweb.job.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final ResumeMapper resumeMapper;

    public ResumeService(ResumeRepository resumeRepository,
                         UserRepository userRepository,
                         JobRepository jobRepository,
                         ResumeMapper resumeMapper){
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.resumeMapper = resumeMapper;
    }

    public Page<ResumeResponse> getAllResumes(Specification<Resume> spec,
                                              Pageable pageable){
        return resumeRepository.findAll(spec, pageable)
                .map(resumeMapper::toResumeResponse);
    }

    public ResumeResponse getResumeById(long resumeId){
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new AppException(ErrorCode.RESUME_NOT_EXISTED));

        return resumeMapper.toResumeResponse(resume);
    }

    public ResumeResponse createResume(ResumeCreationRequest request){
        Resume newResume = resumeMapper.toResume(request);

        User user = userRepository.findById(request.getUser().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        newResume.setUser(user);

        Job job = jobRepository.findById(request.getJob().getId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_EXISTED));
        newResume.setJob(job);

        String email = getEmailInToken();
        newResume.setCreatedBy(email);
        newResume.setCreatedAt(Instant.now());
        resumeRepository.save(newResume);

        return resumeMapper.toResumeResponse(newResume);
    }

    public ResumeResponse updateResume(long resumeId, ResumeUpdateRequest request){
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new AppException(ErrorCode.RESUME_NOT_EXISTED));

        resumeMapper.updateResume(resume, request);
        String email = getEmailInToken();
        resume.setUpdatedBy(email);
        resume.setUpdatedAt(Instant.now());
        resumeRepository.save(resume);

        return resumeMapper.toResumeResponse(resume);
    }

    public void deleteResume(long resumeId){
        resumeRepository.deleteById(resumeId);
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
