package com.jobweb.job.service;

import com.jobweb.job.domain.Company;
import com.jobweb.job.domain.dto.request.CompanyCreationRequest;
import com.jobweb.job.domain.dto.request.CompanyUpdateRequest;
import com.jobweb.job.domain.dto.response.CompanyResponse;
import com.jobweb.job.mapper.CompanyMapper;
import com.jobweb.job.repository.CompanyRepository;
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
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository,
                          CompanyMapper companyMapper){
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public Page<CompanyResponse> getAllCompanies(Specification<Company> spec, Pageable pageable){
        return companyRepository.findAll(spec, pageable)
                .map(companyMapper::toCompanyResponse);
    }

    public CompanyResponse getCompanyById(long companyId){
        return companyMapper.toCompanyResponse(
                companyRepository.findById(companyId).get());
    }

    public CompanyResponse createCompany(CompanyCreationRequest request){
        Optional<Company> company = companyRepository.findByName(request.getName());
        if(company.isPresent())
            throw new RuntimeException();

        Company newCompany = companyMapper.toCompany(request);
        var auth = SecurityContextHolder.getContext().getAuthentication();
        // kiem tra auth co phai la "the hien" cua JwtA.... neu dung thi gan auth = jwtAuth
        if(auth instanceof JwtAuthenticationToken jwtAuth){
            String email = jwtAuth.getToken().getClaimAsString("iss");
            newCompany.setCreatedBy(email);
        }
        newCompany.setCreatedAt(Instant.now());
        companyRepository.save(newCompany);

        return companyMapper.toCompanyResponse(newCompany);
    }

    public CompanyResponse updateCompany(long companyId, CompanyUpdateRequest request){
        Optional<Company> company = companyRepository.findByName(request.getName());
        if(company.isEmpty())
            throw new RuntimeException();

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth instanceof JwtAuthenticationToken jwtAuth){
            String email = jwtAuth.getToken().getClaimAsString("iss");
            company.get().setUpdatedBy(email);
        }
        company.get().setUpdatedAt(Instant.now());

        companyMapper.updateCompany(company.get(), request);
        return companyMapper.toCompanyResponse(companyRepository.save(company.get()));
    }

    public void deleteCompany(long companyId){
        companyRepository.deleteById(companyId);
    }

}
