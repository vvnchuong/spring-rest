package com.jobweb.job.domain.dto.request;

import com.jobweb.job.enums.Level;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCreationRequest {

    String name;
    String location;
    double salary;
    int quantity;
    Level level;
    String description;
    Instant startDate;
    Instant endDate;
    boolean active;

    List<SkillJob> skills;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SkillJob {
        long id;
    }

}
