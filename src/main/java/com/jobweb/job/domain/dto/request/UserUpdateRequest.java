package com.jobweb.job.domain.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String name;
    String password;
    int age;
    String gender;
    String address;

    RoleUser role;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RoleUser {
        long id;
    }

}