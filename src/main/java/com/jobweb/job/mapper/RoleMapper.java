package com.jobweb.job.mapper;

import com.jobweb.job.domain.Role;
import com.jobweb.job.domain.dto.request.RoleCreationRequest;
import com.jobweb.job.domain.dto.request.RoleUpdateRequest;
import com.jobweb.job.domain.dto.response.PermissionResponse;
import com.jobweb.job.domain.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(RoleCreationRequest request);

    default RoleResponse toRoleResponse(Role role){
        if(role == null)
            return null;

        List<PermissionResponse> permissions = null;
        if(role.getPermissions() != null){
           permissions = role.getPermissions().stream()
                   .map(per -> PermissionResponse.builder()
                           .id(per.getId())
                           .name(per.getName())
                           .apiPath(per.getApiPath())
                           .method(per.getMethod())
                           .module(per.getModule())
                           .createdAt(per.getCreatedAt())
                           .createdBy(per.getCreatedBy())
                           .updatedAt(per.getUpdatedAt())
                           .updatedBy(per.getUpdatedBy())
                           .build())
                   .toList();
       }

       return RoleResponse.builder()
               .name(role.getName())
               .description(role.getDescription())
               .active(role.isActive())
               .createdAt(role.getCreatedAt())
               .createdBy(role.getCreatedBy())
               .updatedAt(role.getUpdatedAt())
               .updatedBy(role.getUpdatedBy())
               .permissions(permissions)
               .build();
    }

    void updateRole(@MappingTarget Role role, RoleUpdateRequest request);

}
