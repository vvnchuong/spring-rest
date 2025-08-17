package com.jobweb.job.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillResponse {

    String name;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

}
