package com.jobweb.job.domain;

import com.jobweb.job.enums.Level;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    String location;
    double salary;
    int quantity;

    @Enumerated(EnumType.STRING)
    Level level;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    Instant startDate;
    Instant endDate;
    boolean active;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    @ManyToMany
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"))
    List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    List<Resume> resumes = new ArrayList<>();

}
