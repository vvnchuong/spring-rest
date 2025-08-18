package com.jobweb.job.service;

import com.jobweb.job.domain.Permission;
import com.jobweb.job.domain.Role;
import com.jobweb.job.domain.dto.request.RoleCreationRequest;
import com.jobweb.job.domain.dto.request.RoleUpdateRequest;
import com.jobweb.job.domain.dto.response.RoleResponse;
import com.jobweb.job.enums.ErrorCode;
import com.jobweb.job.exception.AppException;
import com.jobweb.job.mapper.RoleMapper;
import com.jobweb.job.repository.PermissionRepository;
import com.jobweb.job.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository,
                       PermissionRepository permissionRepository,
                       RoleMapper roleMapper){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    public Page<RoleResponse> getAllRole(Specification<Role> spec,
                                         Pageable pageable){
        return roleRepository.findAll(spec, pageable)
                .map(roleMapper::toRoleResponse);
    }

    public RoleResponse getRoleById(long roleId){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse createRole(RoleCreationRequest request){
        boolean isExisted = roleRepository.existsByName(request.getName());
        if(isExisted)
            throw new AppException(ErrorCode.ROLE_EXISTED);

        List<Long> perId = request.getPermissions().stream()
                .map(RoleCreationRequest.PermissionRole::getId)
                .toList();

        List<Permission> permissions = permissionRepository.findByIdIn(perId);
        if(permissions.isEmpty())
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);

        Role newRole = roleMapper.toRole(request);
        newRole.setPermissions(permissions);

        String email = getEmailInToken();
        newRole.setCreatedBy(email);
        newRole.setCreatedAt(Instant.now());

        return roleMapper.toRoleResponse(roleRepository.save(newRole));
    }

    public RoleResponse updateRole(long roleId, RoleUpdateRequest request){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        List<Long> perId = request.getPermissions().stream()
                .map(RoleUpdateRequest.PermissionRole::getId)
                .toList();

        List<Permission> permissions = permissionRepository.findByIdIn(perId);
        if(permissions.isEmpty())
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);

        roleMapper.updateRole(role, request);
        role.setPermissions(permissions);

        String email = getEmailInToken();
        role.setUpdatedBy(email);
        role.setUpdatedAt(Instant.now());

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public void deleteRole(long roleId){
        Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleRepository.deleteById(roleId);
    }

    private String getEmailInToken(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(!(auth instanceof JwtAuthenticationToken jwtAuth)){
            throw new RuntimeException("Token invalid");
        }else{
            return jwtAuth.getToken().getClaimAsString("iss");

        }
    }

}
