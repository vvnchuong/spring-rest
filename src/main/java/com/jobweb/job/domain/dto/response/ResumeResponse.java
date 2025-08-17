package com.jobweb.job.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResumeResponse {

    long id;
    String email;
    String url;
    String status;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;

    UserResume user;
    JobResume job;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserResume {
        long id;
        String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JobResume {
        long id;
        String name;
    }

}
