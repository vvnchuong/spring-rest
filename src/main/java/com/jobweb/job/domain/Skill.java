package com.jobweb.job.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToMany(mappedBy = "skills")
    private List<Job> jobs;

}
