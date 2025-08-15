package com.jobweb.job.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyCreationRequest {

    String name;
    String description;
    String address;
    String logo;

}
