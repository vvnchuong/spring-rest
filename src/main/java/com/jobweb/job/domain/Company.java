package com.jobweb.job.domain;

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
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    String address;
    String logo;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

}
