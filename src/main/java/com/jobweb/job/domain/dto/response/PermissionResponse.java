package com.jobweb.job.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {

    long id;
    String name;
    String apiPath;
    String method;
    String module;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;

}
