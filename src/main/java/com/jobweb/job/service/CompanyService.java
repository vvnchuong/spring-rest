package com.jobweb.job.service;

import com.jobweb.job.domain.Company;
import com.jobweb.job.domain.dto.request.CompanyCreationRequest;
import com.jobweb.job.domain.dto.request.CompanyUpdateRequest;
import com.jobweb.job.domain.dto.response.CompanyResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.CompanyMapper;
import com.jobweb.job.repository.CompanyRepository;
import com.jobweb.job.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository,
                          UserRepository userRepository,
                          CompanyMapper companyMapper){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyMapper = companyMapper;
    }

    public Page<CompanyResponse> getAllCompanies(Specification<Company> spec, Pageable pageable){
        return companyRepository.findAll(spec, pageable)
                .map(companyMapper::toCompanyResponse);
    }

    public CompanyResponse getCompanyById(long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));
        return companyMapper.toCompanyResponse(company);
    }

    public CompanyResponse createCompany(CompanyCreationRequest request){
        Company company = companyRepository.findByName(request.getName())
                .orElseThrow(() -> new AppException(ErrorCode.COMPANY_EXISTED));

        Company newCompany = companyMapper.toCompany(request);

        String email = getEmailInToken();
        newCompany.setCreatedBy(email);
        newCompany.setCreatedAt(Instant.now());
        companyRepository.save(newCompany);

        return companyMapper.toCompanyResponse(newCompany);
    }

    public CompanyResponse updateCompany(long companyId, CompanyUpdateRequest request){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String email = getEmailInToken();
        company.setUpdatedBy(email);
        company.setUpdatedAt(Instant.now());

        companyMapper.updateCompany(company, request);
        return companyMapper.toCompanyResponse(companyRepository.save(company));
    }

    @Transactional
    public void deleteCompany(long companyId){
        Company company = companyRepository.findById(companyId)
                        .orElseThrow(() -> new AppException(ErrorCode.COMPANY_NOT_EXISTED));

        userRepository.deleteAll();
        companyRepository.deleteById(companyId);
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
