package com.jobweb.job.domain.dto.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
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

}