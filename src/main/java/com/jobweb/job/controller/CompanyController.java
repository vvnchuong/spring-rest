package com.jobweb.job.controller;

import com.jobweb.job.domain.Company;
import com.jobweb.job.domain.dto.request.CompanyCreationRequest;
import com.jobweb.job.domain.dto.request.CompanyUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.CompanyResponse;
import com.jobweb.job.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping
    public ApiResponse<List<CompanyResponse>> getAllCompanies(
            @Filter Specification<Company> spec, Pageable pageable){

//        int currentPage = Integer.parseInt(currentOptional.get());
//        int pageSize = Integer.parseInt(pageSizeOptional.get());
//
//        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        return ApiResponse.<List<CompanyResponse>>builder()
                .data(companyService.getAllCompanies(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> getCompanyById(
            @PathVariable("id") long companyId){
        return ApiResponse.<CompanyResponse>builder()
                .data(companyService.getCompanyById(companyId))
                .build();
    }

    @PostMapping
    public ApiResponse<CompanyResponse> createCompany(
            @RequestBody CompanyCreationRequest request){
        return ApiResponse.<CompanyResponse>builder()
                .data(companyService.createCompany(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> updateCompany(
            @PathVariable("id") long companyId,
            @RequestBody CompanyUpdateRequest request){
        return ApiResponse.<CompanyResponse>builder()
                .data(companyService.updateCompany(companyId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCompany(
            @PathVariable("id") long companyId){
        companyService.deleteCompany(companyId);
        return ApiResponse.<Void>builder().build();
    }

}
