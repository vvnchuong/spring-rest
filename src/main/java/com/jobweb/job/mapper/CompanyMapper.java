package com.jobweb.job.mapper;

import com.jobweb.job.domain.Company;
import com.jobweb.job.domain.dto.request.CompanyCreationRequest;
import com.jobweb.job.domain.dto.request.CompanyUpdateRequest;
import com.jobweb.job.domain.dto.response.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toCompany(CompanyCreationRequest request);

    CompanyResponse toCompanyResponse(Company company);

    void updateCompany(@MappingTarget Company company, CompanyUpdateRequest request);

}
