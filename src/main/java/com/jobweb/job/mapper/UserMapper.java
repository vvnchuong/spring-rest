package com.jobweb.job.mapper;

import com.jobweb.job.domain.User;
import com.jobweb.job.domain.dto.request.UserCreationRequest;
import com.jobweb.job.domain.dto.request.UserUpdateRequest;
import com.jobweb.job.domain.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
