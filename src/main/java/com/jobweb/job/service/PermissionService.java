package com.jobweb.job.service;

import com.jobweb.job.domain.Permission;
import com.jobweb.job.domain.dto.request.PermissionCreationRequest;
import com.jobweb.job.domain.dto.request.PermissionUpdateRequest;
import com.jobweb.job.domain.dto.response.PermissionResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.PermissionMapper;
import com.jobweb.job.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository,
                             PermissionMapper permissionMapper){
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public Page<PermissionResponse> getAllPermissions(Specification<Permission> spec,
                                                      Pageable pageable){
        return permissionRepository.findAll(spec, pageable)
                .map(permissionMapper::toPermissionResponse);
    }

    public PermissionResponse getPermissionById(long permissionId){
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));

        return permissionMapper.toPermissionResponse(permission);
    }

    public PermissionResponse createPermission(PermissionCreationRequest request){
        boolean isExisted = permissionRepository.existsConflict(request).isPresent();
        if(isExisted)
            throw new AppException(ErrorCode.PERMISSION_EXISTED);

        Permission newPermission = permissionMapper.toPermission(request);

        String email = getEmailInToken();
        newPermission.setCreatedBy(email);
        newPermission.setCreatedAt(Instant.now());

        return permissionMapper.toPermissionResponse(
                permissionRepository.save(newPermission));
    }

    public PermissionResponse updatePermission(long permissionId,
                                               PermissionUpdateRequest request){
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));

        permissionMapper.updatePermission(permission, request);

        String email = getEmailInToken();
        permission.setUpdatedBy(email);
        permission.setUpdatedAt(Instant.now());

        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public void deletePermission(long permissionId){
        Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_EXISTED));
        permissionRepository.deleteById(permissionId);
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
