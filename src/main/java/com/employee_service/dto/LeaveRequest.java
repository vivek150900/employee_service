package com.employee_service.dto;


import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveRequest {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
}
