package com.employee_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String city;
    private String contNo;
    private LocalDate dob;
    private String gender;


    // Automatically stores record creation time
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Automatically updates when record is modified
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Stores which user created the record
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    private String deletedBy;


//    @CreationTimestamp
//    @UpdateTimestamp
//    @CreatedBy
//    Soft Deletion
}
