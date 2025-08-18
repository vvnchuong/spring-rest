package com.jobweb.job.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionUpdateRequest {

    String name;
    String apiPath;
    String method;
    String module;

}
