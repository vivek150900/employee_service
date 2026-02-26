package com.employee_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data

public class EmployeeRequest {

    private Long id;

    @NotBlank(message = "Name should be present")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should be present")
    private String email;

    @NotBlank(message = "City should be present")
    private String city;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contNo;

    @NotNull(message = "DOB should be present")
    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Gender should be present")
    private String gender;

}
