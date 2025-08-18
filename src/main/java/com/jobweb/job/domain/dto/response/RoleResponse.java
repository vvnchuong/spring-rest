package com.jobweb.job.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {

    String name;
    String description;
    boolean active;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;

    List<PermissionResponse> permissions;

}
