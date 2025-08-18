package com.jobweb.job.domain;

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
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    String apiPath;
    String method;
    String module;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

//    @ManyToMany
//    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "permission_id"))
//    List<Role> roles = new ArrayList<>();

}
