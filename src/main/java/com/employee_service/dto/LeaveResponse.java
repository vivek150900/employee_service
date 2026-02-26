package com.employee_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveResponse {

    private Long id;
    private String employeeEmail;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
    private String status;
    private LocalDateTime appliedAt;
}
