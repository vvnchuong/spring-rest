package com.jobweb.job.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {

    String name;
    String description;
    boolean active;

    List<PermissionRole> permissions;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PermissionRole {
        long id;
    }

}
