package com.employee_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String city;
    private String contNo;
    private LocalDate dob;
    private String gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private String deletedBy;
}

