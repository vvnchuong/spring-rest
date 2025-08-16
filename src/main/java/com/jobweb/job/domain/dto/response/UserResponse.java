package com.jobweb.job.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    long id;
    String name;
    String email;
    int age;
    String gender;
    String address;
    Instant createdAt;

    CompanyUser company;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CompanyUser {
        long id;
        String name;
    }

}
