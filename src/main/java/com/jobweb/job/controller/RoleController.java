package com.jobweb.job.controller;

import com.jobweb.job.domain.Role;
import com.jobweb.job.domain.dto.request.RoleCreationRequest;
import com.jobweb.job.domain.dto.request.RoleUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.RoleResponse;
import com.jobweb.job.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles(
            @Filter Specification<Role> spec,
            Pageable pageable){
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRole(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(
            @PathVariable("id") long roleId){
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.getRoleById(roleId))
                .build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> createRole(
            @RequestBody RoleCreationRequest request){
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable("id") long roleId,
            @RequestBody RoleUpdateRequest request){
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.updateRole(roleId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(
            @PathVariable("id") long roleId){
        roleService.deleteRole(roleId);
        return ApiResponse.<Void>builder().build();
    }

}
