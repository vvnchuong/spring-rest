package com.jobweb.job.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyUpdateRequest {

    String name;
    String description;
    String address;
    String logo;
    Instant updatedAt;
    String updatedBy;

}
