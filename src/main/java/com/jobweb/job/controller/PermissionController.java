package com.jobweb.job.controller;

import com.jobweb.job.domain.Permission;
import com.jobweb.job.domain.dto.request.PermissionCreationRequest;
import com.jobweb.job.domain.dto.request.PermissionUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.PermissionResponse;
import com.jobweb.job.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions(
            @Filter Specification<Permission> spec, Pageable pageable){
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermissions(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> getPermissionById(
            @PathVariable("id") long perId){
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.getPermissionById(perId))
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(
            @RequestBody PermissionCreationRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.createPermission(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionResponse> updatePermission(
            @PathVariable("id") long perId,
            @RequestBody PermissionUpdateRequest request){
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.updatePermission(perId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") long perId){
        permissionService.deletePermission(perId);
        return ApiResponse.<Void>builder().build();
    }

}
