package com.jobweb.job.mapper;

import com.jobweb.job.domain.Permission;
import com.jobweb.job.domain.dto.request.PermissionCreationRequest;
import com.jobweb.job.domain.dto.request.PermissionUpdateRequest;
import com.jobweb.job.domain.dto.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionCreationRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    void updatePermission(@MappingTarget Permission permission, PermissionUpdateRequest request);

}
