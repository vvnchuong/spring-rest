package com.jobweb.job.controller;

import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.UserCreationRequest;
import com.jobweb.job.domain.dto.request.UserUpdateRequest;
import com.jobweb.job.domain.dto.response.ApiResponse;
import com.jobweb.job.domain.dto.response.UserResponse;
import com.jobweb.job.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable){
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers(spec, pageable).getContent())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserUserById(
            @PathVariable("id") long userId){
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(userId))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("id") long userId,
            @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @PathVariable("id") long userId){
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder().build();
    }


}
