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

    default UserResponse toUserResponse(User user) {
        if (user == null)
            return null;

        UserResponse.CompanyUser companyUser = null;
        if (user.getCompany() != null) {
            companyUser = UserResponse.CompanyUser.builder()
                    .id(user.getCompany().getId())
                    .name(user.getCompany().getName())
                    .build();
        }

        UserResponse.RoleUser roleUser = null;
        if(user.getRole() != null){
            roleUser = UserResponse.RoleUser.builder()
                    .id(user.getRole().getId())
                    .name(user.getRole().getName())
                    .build();
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender().name())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .company(companyUser)
                .role(roleUser)
                .build();
    }

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
