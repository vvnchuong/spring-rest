package com.jobweb.job.domain;

import com.jobweb.job.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    String email;
    String password;
    int age;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;
    String refreshToken;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

}
