package com.jobweb.job.domain.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String name;
    String email;
    String password;
    int age;
    String gender;
    String address;

}
